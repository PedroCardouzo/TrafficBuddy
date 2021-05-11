package SpeedRadarUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpeedRadarController implements ISpeedRadarController {
  private List<ISpeedRadarDriver> speedRadarDrivers;
  private IInfractionHistory infractionHistory;

  public SpeedRadarController(IInfractionHistory infractionHistory) {
    this.speedRadarDrivers = new ArrayList<>();
    this.infractionHistory = infractionHistory;
  }

  public void attachSpeedRadar(final ISpeedRadarDriver srd) {
    this.speedRadarDrivers.add(srd);
  }

  public void detachSpeedRadar(final int index) {
    this.speedRadarDrivers.remove(index);
  }

  public List<String> collectInfractionHistory() {
    return this.infractionHistory.getLogs();
  }

  public void clearInfractionHistory() {
    this.infractionHistory.clear();
  }

  private void gatherInfractionsIntoHistory() {
    this.speedRadarDrivers.stream().map(ISpeedRadarDriver::getInfractions).map(Objects::toString).forEach(infractionHistory::log);
  }
}
