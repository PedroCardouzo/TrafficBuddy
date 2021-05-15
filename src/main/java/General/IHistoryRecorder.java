package General;

import org.json.JSONObject;

import java.util.List;

public interface IHistoryRecorder {
  void log(final JSONObject logEntry);

  List<JSONObject> getLogs();

  void clear();
}
