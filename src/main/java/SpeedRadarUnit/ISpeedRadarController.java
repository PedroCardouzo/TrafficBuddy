package SpeedRadarUnit;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface ISpeedRadarController {
  void attachSpeedRadar(SpeedRadarDriver speedRadarDriver);

  Map<String, String> getSpeedRadarData(String selectedSpeedRadar);

  void setSpeedRadarData(Map<String, String> speedRadarData);

  List<JSONObject> collectInfractionHistory();

  void clearInfractionHistory();
}
