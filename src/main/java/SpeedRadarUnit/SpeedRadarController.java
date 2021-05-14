package SpeedRadarUnit;

import General.CustomConstants;
import SemaphoreUnit.ISemaphoreDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpeedRadarController implements ISpeedRadarController {
  private final ScheduledExecutorService es;
  private int infractionHistoryPollPeriodSeconds;
  private List<ISpeedRadarDriver> speedRadarDrivers;
  private final IInfractionHistory infractionHistory;
  private ScheduledFuture<?> infractionPollTask;


  public SpeedRadarController(IInfractionHistory infractionHistory, final int infractionHistoryPollPeriodSeconds) {
    this.speedRadarDrivers = new ArrayList<>();
    this.infractionHistory = infractionHistory;
    this.es = Executors.newSingleThreadScheduledExecutor();
    this.infractionHistoryPollPeriodSeconds = infractionHistoryPollPeriodSeconds;
  }

  public void setInfractionHistoryPollPeriod(final int infractionHistoryPollPeriodSeconds) {
    this.infractionHistoryPollPeriodSeconds = infractionHistoryPollPeriodSeconds;
    while (!this.infractionPollTask.cancel(false));
    this.start();
  }

  public synchronized void attachSpeedRadar(final ISpeedRadarDriver srd) {
    this.speedRadarDrivers.add(srd);
  }

  public synchronized void detachSpeedRadar(final int index) {
    this.speedRadarDrivers.remove(index);
  }

  public void start() {
    this.infractionPollTask = this.es.scheduleAtFixedRate(this::gatherInfractionsIntoHistory, 0, this.infractionHistoryPollPeriodSeconds, TimeUnit.SECONDS);
  }

  public void stop() {
    while (!this.infractionPollTask.cancel(false));
  }

  public List<String> collectInfractionHistory() {
    return this.infractionHistory.getLogs();
  }

  public void clearInfractionHistory() {
    this.infractionHistory.clear();
  }

  private synchronized void gatherInfractionsIntoHistory() {
    this.speedRadarDrivers.stream().map(ISpeedRadarDriver::getInfractions).map(Objects::toString).forEach(infractionHistory::log);
  }

  @Override
  public void attachSpeedRadar(final SpeedRadarDriver speedRadarDriver) {
    this.speedRadarDrivers.add(speedRadarDriver);
  }

  @Override
  public Map<String, String> getSpeedRadarData(final String selectedSpeedRadar) {
    for (final ISpeedRadarDriver sd : speedRadarDrivers) {
      if (sd.getIp().equals(selectedSpeedRadar)) {
        Map<String, String> newSpeedRadarData = new HashMap<>();
        newSpeedRadarData.put(CustomConstants.IP_ADDRESS, sd.getIp());
        newSpeedRadarData.put(CustomConstants.DEVICE_DESCRIPTION, sd.getDescription());
        newSpeedRadarData.put(CustomConstants.SPEED_LIMIT, String.valueOf(sd.getSpeedLimit()));

        return newSpeedRadarData;
      }
    }

    return Collections.emptyMap();
  }

  @Override
  public void setSpeedRadarData(Map<String, String> newSpeedRadarData) {
    final String ip = newSpeedRadarData.getOrDefault(CustomConstants.IP_ADDRESS, "");
    ISpeedRadarDriver speedRadar = speedRadarDrivers.stream().filter((ISpeedRadarDriver x) -> x.getIp().equals(ip)).findFirst().orElse(null);

    if (speedRadar != null) {
      speedRadar.setDescription(newSpeedRadarData.getOrDefault(CustomConstants.DEVICE_DESCRIPTION, ""));
      final int newSpeedLimit = Integer.parseInt(newSpeedRadarData.getOrDefault(CustomConstants.SPEED_LIMIT, "0"));

      if (newSpeedLimit != 0)
        speedRadar.setSpeedLimit(newSpeedLimit);
    }
  }
}
