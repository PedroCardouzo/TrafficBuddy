package SpeedRadarUnit;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InfractionHistory implements IInfractionHistory {
  List<JSONObject> history;

  public InfractionHistory() {
    this.history = new ArrayList<>();
  }

  @Override
  public void log(JSONObject logEntry) {
    this.history.add(logEntry);
  }

  @Override
  public List<JSONObject> getLogs() {
    return this.history;
  }

  @Override
  public void clear() {
    this.history = new ArrayList<>();
  }
}
