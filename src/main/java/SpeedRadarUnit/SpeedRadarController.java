package SpeedRadarUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
}
