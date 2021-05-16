package App;

import java.util.List;
import java.util.Map;

public interface IAppManager {
  void attachSemaphore(String ipAddress, String description);

  List<String> getSemaphoreList();

  void attachSpeedRadar(String ipAddress, String description);

  void attachDisplay(String ipAddress, String description, String selectedSemaphoreForDisplay);

  List<String> getSpeedRadarList();

  List<String> getDisplayList();

  void setSemaphoreData(Map<String, String> newSemaphoreData);

  Map<String, String> getSemaphoreData(String ipAddress);

  Map<String, String> getSpeedRadarData(String ipAddress);

  void processInfractions();

  void setSpeedRadarData(Map<String, String> newSpeedRadar);

  void start();

  void stop();

  Map<String, String> getDisplayData(String ipAddress);

  void setDisplayData(Map<String, String> newDisplayData);
}
