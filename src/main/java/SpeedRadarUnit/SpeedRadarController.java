package SpeedRadarUnit;

import General.CustomConstants;
import General.IHistoryRecorder;
import org.json.JSONObject;

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

import static java.lang.Integer.parseInt;

public class SpeedRadarController implements ISpeedRadarController {
  private final ScheduledExecutorService es;
  private int infractionHistoryPollPeriodSeconds;
  private List<ISpeedRadarDriver> speedRadarDrivers;
  private final IHistoryRecorder infractionHistory;
  private ScheduledFuture<?> infractionPollTask;


  public SpeedRadarController(IHistoryRecorder infractionHistory) {
    this.speedRadarDrivers = new ArrayList<>();
    this.infractionHistory = infractionHistory;
    this.es = Executors.newSingleThreadScheduledExecutor();
    this.infractionHistoryPollPeriodSeconds = CustomConstants.DEFAULT_INFRACTION_HISTORY_POLL_PERIOD_SECONDS;
  }

  @Override
  public void setInfractionHistoryPollPeriod(final int infractionHistoryPollPeriodSeconds) {
    this.infractionHistoryPollPeriodSeconds = infractionHistoryPollPeriodSeconds;
    while (this.infractionPollTask != null && !this.infractionPollTask.cancel(false));
    this.start();
  }

  public synchronized void detachSpeedRadar(final int index) {
    this.speedRadarDrivers.remove(index);
  }

  @Override
  public void start() {
    this.infractionPollTask = this.es.scheduleAtFixedRate(this::gatherInfractionsIntoHistory, 0, this.infractionHistoryPollPeriodSeconds, TimeUnit.SECONDS);
  }

  @Override
  public void stop() {
    while (!this.infractionPollTask.cancel(false));
  }

  @Override
  public List<String> getSpeedRadarList() {
    return speedRadarDrivers.stream().map(ISpeedRadarDriver::getIp).collect(Collectors.toList());
  }

  @Override
  public List<JSONObject> collectInfractionHistory() {
    return this.infractionHistory.getLogs();
  }

  @Override
  public void clearInfractionHistory() {
    this.infractionHistory.clear();
  }

  private synchronized void gatherInfractionsIntoHistory() {
    this.speedRadarDrivers.stream().map(ISpeedRadarDriver::getInfractions).forEach((List<Infraction> infractionList) -> infractionList.forEach(x -> infractionHistory.log(x.toJson())));
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
        newSpeedRadarData.put(CustomConstants.DEFAULT_INFRACTION_HISTORY_POLL_PERIOD_SECONDS_JSON, String.valueOf(this.infractionHistoryPollPeriodSeconds));
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
      final int newSpeedLimit = parseInt(newSpeedRadarData.getOrDefault(CustomConstants.SPEED_LIMIT, "0"));

      if (newSpeedLimit != 0) {
        speedRadar.setSpeedLimit(newSpeedLimit);
      }

      final int newInfractionHistoryPollPeriod = Integer.parseInt(newSpeedRadarData.getOrDefault(CustomConstants.DEFAULT_INFRACTION_HISTORY_POLL_PERIOD_SECONDS_JSON, String.valueOf(this.infractionHistoryPollPeriodSeconds)));
      this.setInfractionHistoryPollPeriod(newInfractionHistoryPollPeriod);
    }
  }
}
