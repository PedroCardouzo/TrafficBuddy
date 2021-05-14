package SpeedRadarUnit;

import org.json.JSONObject;

import java.util.List;

public interface IInfractionHistory {
  void log(final JSONObject logEntry);

  List<JSONObject> getLogs();

  void clear();
}
