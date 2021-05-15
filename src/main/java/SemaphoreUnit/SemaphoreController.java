package SemaphoreUnit;

import General.CustomConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SemaphoreController implements ISemaphoreController {
  private List<ISemaphoreDriver> semaphoreDrivers;
  private List<ITrafficCameraDriver> trafficCameraDrivers;
  private List<Integer> openTimings;
  private final ISemaphoreHistory semaphoreHistory;
  private final ScheduledExecutorService es;
  private int currentOpen;
  private boolean active;
  private ScheduledFuture<?> periodicTask;
  private int currentReestimationInterval;

  public SemaphoreController(final ISemaphoreHistory semaphoreHistory) {
    this.active = false;
    this.semaphoreDrivers = new ArrayList<>();
    this.trafficCameraDrivers = new ArrayList<>();
    this.openTimings = new ArrayList<>();
    this.es = Executors.newSingleThreadScheduledExecutor();
    this.semaphoreHistory = semaphoreHistory;
    this.currentOpen = -1;
  }

  @Override
  public void attachSemaphore(final ISemaphoreDriver sd, final ITrafficCameraDriver tcd) {
    this.semaphoreDrivers.add(sd);
    this.trafficCameraDrivers.add(tcd);

    synchronized (this) {
      this.openTimings.add(CustomConstants.DEFAULT_OPEN_TIMING);
    }
  }

  public void detachSemaphore(final int index) {
    this.semaphoreDrivers.remove(index);
    this.trafficCameraDrivers.remove(index);
    synchronized (this) {
      this.openTimings.remove(index);
    }
  }

  @Override
  public String toString() {
    List<Integer> copyTimings;
    synchronized (this) {
      copyTimings = new ArrayList<>(this.openTimings);
    }
    final StringBuilder sb = new StringBuilder();

    for (int i=0; i < copyTimings.size(); i++) {
      sb.append(String.format("Semaphore %s: Open length: %s | Traffic flux: %s\n", i, copyTimings.get(i), this.trafficCameraDrivers.get(i).getTrafficFlux()));
    }

    return sb.toString();
  }

  @Override
  public void changeReestimationInterval(final int newInterval) {
    if (newInterval == this.currentReestimationInterval && this.active)
      return;

    this.stop();
    this.start(newInterval);
  }

  @Override
  public void start() {
    this.start(CustomConstants.CONF_REESTIMATION_INTERVAL);
  }

  public void start(final int interval) {
    this.active = true;
    this.currentReestimationInterval = interval;

    while (this.periodicTask != null && !this.periodicTask.cancel(false));

    this.periodicTask = this.es.scheduleAtFixedRate(this::reestimateTimings, 0, interval, TimeUnit.SECONDS);
    this.next();
  }

  @Override
  public void stop() {
    this.active = false;
    this.semaphoreDrivers.forEach(ISemaphoreDriver::close);
  }

  @Override
  public ISemaphoreDriver getSemaphore(String ipAddress) {
    return semaphoreDrivers.stream().filter((ISemaphoreDriver x) -> x.getIp().equals(ipAddress)).findFirst().orElse(null);
  }

  @Override
  public Map<String, String> getSemaphoreData(final String target) {
    for (int i = 0; i < semaphoreDrivers.size(); i++) {
      final ISemaphoreDriver sd = semaphoreDrivers.get(i);

      if (sd.getIp().equals(target)) {
        Map<String, String> newSemaphoreData = new HashMap<>();
        newSemaphoreData.put(CustomConstants.IP_ADDRESS, sd.getIp());
        newSemaphoreData.put(CustomConstants.TRAFFIC_FLUX, String.valueOf(trafficCameraDrivers.get(i).getTrafficFlux()));
        newSemaphoreData.put(CustomConstants.DEVICE_DESCRIPTION, sd.getDescription());

        synchronized (this) {
          newSemaphoreData.put(CustomConstants.CYCLE_PERIOD, String.valueOf(openTimings.stream().mapToInt(Integer::intValue).sum()));
          newSemaphoreData.put(CustomConstants.SEMAPHORE_TIMING, String.valueOf(openTimings.get(i)));
        }

        newSemaphoreData.put(CustomConstants.CONF_REESTIMATION_INTERVAL_JSON, String.valueOf(this.currentReestimationInterval));

        return newSemaphoreData;
      }
    }

    return Collections.emptyMap();
  }

  @Override
  public void setSemaphoreData(Map<String, String> newSemaphoreData) {
    final String ip = newSemaphoreData.getOrDefault("ipAddress", "");
    ISemaphoreDriver semaphore = semaphoreDrivers.stream().filter((ISemaphoreDriver x) -> x.getIp().equals(ip)).findFirst().orElse(null);

    if (semaphore != null) {
      final int index = semaphoreDrivers.indexOf(semaphore);
      semaphore.setDescription(newSemaphoreData.getOrDefault(CustomConstants.DEVICE_DESCRIPTION, ""));
      trafficCameraDrivers.get(index).setTrafficFlux(Integer.parseInt(newSemaphoreData.get(CustomConstants.TRAFFIC_FLUX)));
      final int newSum = Integer.parseInt(newSemaphoreData.get("cyclePeriod"));

      synchronized (this) {
        final int sum = openTimings.stream().mapToInt(Integer::intValue).sum();
        this.openTimings = openTimings.stream().map(x -> x * newSum / sum).collect(Collectors.toList()); // scale timings equally
      }

      final int newReestimationTiming = Integer.parseInt(newSemaphoreData.getOrDefault(CustomConstants.CONF_REESTIMATION_INTERVAL_JSON, "0"));
      this.changeReestimationInterval(newReestimationTiming);
    }
  }

  @Override
  public List<String> getSemaphoreList() {
    return semaphoreDrivers.stream().map(ISemaphoreDriver::getIp).collect(Collectors.toList());
  }

  private void next() {
    if (!active)
      return;

    if (this.currentOpen != -1) {
      this.semaphoreDrivers.get(this.currentOpen).close();
    }

    if (this.semaphoreDrivers.size()-1 == this.currentOpen) {
      this.currentOpen = 0;
    } else {
      this.currentOpen++;
    }

    this.semaphoreDrivers.get(this.currentOpen).open();

    int timing;
    synchronized (this) {
      timing = this.openTimings.get(currentOpen);
    }

    this.es.schedule(this::next, timing, TimeUnit.SECONDS);
  }

  public List<String> getHistory() {
    return this.semaphoreHistory.getLogs();
  }

  // simplified algorithm for best timings
  private void reestimateTimings() {
    final List<Integer> trafficFlux = trafficCameraDrivers.stream().map(ITrafficCameraDriver::getTrafficFlux).collect(Collectors.toList());
    final int totalFlux = trafficFlux.stream().mapToInt(Integer::intValue).sum();
    // calculate relative flux
    synchronized (this) {
      final int totalTime = openTimings.stream().mapToInt(Integer::intValue).sum();

      this.openTimings = trafficFlux.stream().map(flux -> totalTime * flux / totalFlux).collect(Collectors.toList());
    }

    for (int i = 0; i < semaphoreDrivers.size(); i++) {
      final ISemaphoreDriver sd = semaphoreDrivers.get(i);
      final ITrafficCameraDriver tcd = trafficCameraDrivers.get(i);

      sd.setFluxIntensityMessage(this.getFluxIntensity(tcd.getTrafficFlux(), totalFlux));
    }

    this.semaphoreHistory.log(this.toString());
  }

  private String getFluxIntensity(int trafficFlux, int totalFlux) {
    final float percentage = (float) 100*trafficFlux / totalFlux;

    if (percentage >= CustomConstants.FLUX_HIGH_PERCENTAGE) {
      return CustomConstants.FLUX_VERY_HIGH_PERCENTAGE_MESSAGE;
    } else if (percentage >= CustomConstants.FLUX_MEDIUM_PERCENTAGE) {
      return CustomConstants.FLUX_HIGH_PERCENTAGE_MESSAGE;
    } else if (percentage >= CustomConstants.FLUX_LOW_PERCENTAGE) {
      return CustomConstants.FLUX_MEDIUM_PERCENTAGE_MESSAGE;
    } else {
      return CustomConstants.FLUX_LOW_PERCENTAGE_MESSAGE;
    }
  }
}
