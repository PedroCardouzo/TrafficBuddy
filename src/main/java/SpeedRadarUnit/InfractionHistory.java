package SpeedRadarUnit;

import java.util.ArrayList;
import java.util.List;

public class InfractionHistory implements IInfractionHistory {
  List<String> history;

  public InfractionHistory() {
    this.history = new ArrayList<>();
  }

  @Override
  public void log(String logEntry) {
    this.history.add(logEntry);
  }

  @Override
  public List<String> getLogs() {
    return this.history;
  }

  @Override
  public void clear() {
    this.history = new ArrayList<>();
  }
}
