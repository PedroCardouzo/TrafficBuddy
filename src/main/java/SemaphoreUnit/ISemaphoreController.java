package SemaphoreUnit;

import DisplayUnit.IDisplayDriver;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface ISemaphoreController {
  void changeReestimationInterval(int newInterval);

  Map<String, String> getSemaphoreData(final String target);

  void setSemaphoreData(Map<String, String> newSemaphoreData);

  List<String> getSemaphoreList();

  void attachSemaphore(final ISemaphoreDriver sd, final ITrafficCameraDriver tcd);

  void start();

  void stop();

  ISemaphoreDriver getSemaphore(String ipAddress);

  JSONObject toJson();

  void attachDisplay(String semaphoreIpAddress, IDisplayDriver dd);
}
