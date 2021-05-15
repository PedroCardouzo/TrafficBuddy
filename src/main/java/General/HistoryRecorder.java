package General;

import General.IHistoryRecorder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryRecorder implements IHistoryRecorder {
  List<JSONObject> history;

  public HistoryRecorder() {
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
