package SemaphoreUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SemaphoreController {
  private static final long CONF_REESTIMATION_INTERVAL = 30;
  private List<SemaphoreDriver> semaphoreDrivers;
  private List<TrafficCameraDriver> trafficCameraDrivers;
  private List<Integer> openTimings;
  private SemaphoreHistory semaphoreHistory;
  private ScheduledExecutorService es;
  private int currentOpen;
  private boolean active;

  public SemaphoreController() {
    this.active = false;
    this.semaphoreDrivers = new ArrayList<>();
    this.trafficCameraDrivers = new ArrayList<>();
    this.openTimings = new ArrayList<>();
    this.es = Executors.newSingleThreadScheduledExecutor();
    this.semaphoreHistory = new SemaphoreHistory();
    this.currentOpen = -1;
  }

  public void attachSemaphore(final SemaphoreDriver sd, final TrafficCameraDriver tcd, final int openTiming) {
    this.semaphoreDrivers.add(sd);
    this.trafficCameraDrivers.add(tcd);

    synchronized (this) {
      this.openTimings.add(openTiming);
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
      sb.append("Semaphore ");
      sb.append(i);
      sb.append(": ");
      sb.append(copyTimings.get(i));
      sb.append('\n');
    }

    return sb.toString();
  }

  public void start() {
    this.active = true;
    this.es.scheduleAtFixedRate(this::reestimateTimings, 0, CONF_REESTIMATION_INTERVAL, TimeUnit.SECONDS);
    this.next();
  }

  public void stop() {
    this.active = false;
    this.semaphoreDrivers.forEach(SemaphoreDriver::close);
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
    return this.semaphoreHistory.getSemaphoreLogs();
  }

  // simplified algorithm for best timings
  private void reestimateTimings() {
    int totalTime;

    synchronized (this) {
      totalTime = openTimings.stream().mapToInt(Integer::intValue).sum();
    }

    final List<Integer> trafficFlux = trafficCameraDrivers.stream().map(TrafficCameraDriver::getTraffixFlux).collect(Collectors.toList());
    final int totalFlux = trafficFlux.stream().mapToInt(Integer::intValue).sum();
    // calculate relative flux
    synchronized (this) {
      this.openTimings = trafficFlux.stream().map(flux -> totalTime * flux / totalFlux).collect(Collectors.toList());
    }

    this.semaphoreHistory.log(this.toString());
  }
}
