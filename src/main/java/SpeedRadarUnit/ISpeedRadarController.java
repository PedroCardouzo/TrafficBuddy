package SpeedRadarUnit;

import java.util.Map;

public interface ISpeedRadarController {
  void attachSpeedRadar(SpeedRadarDriver speedRadarDriver);

  Map<String, String> getSpeedRadarData(String selectedSpeedRadar);

  void setSpeedRadarData(Map<String, String> speedRadarData);
}
