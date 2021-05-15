package SpeedRadarUnit;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface ISpeedRadarController {
  void attachSpeedRadar(SpeedRadarDriver speedRadarDriver);

  Map<String, String> getSpeedRadarData(String selectedSpeedRadar);

  void setSpeedRadarData(Map<String, String> speedRadarData);

  void setInfractionHistoryPollPeriod(int infractionHistoryPollPeriodSeconds);

  List<JSONObject> collectInfractionHistory();

  void clearInfractionHistory();

  void start();

  void stop();

  List<String> getSpeedRadarList();
}
