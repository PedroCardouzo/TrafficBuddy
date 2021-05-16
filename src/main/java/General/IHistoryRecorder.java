package General;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface IHistoryRecorder {
  default void log(Map<String, String> jsonMap) {
    this.log(new JSONObject(jsonMap));
  }

  void log(JSONObject logEntry);

  List<JSONObject> getLogs();

  void clear();
}
