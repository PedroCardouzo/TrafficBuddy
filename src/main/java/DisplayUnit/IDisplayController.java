package DisplayUnit;

import SemaphoreUnit.ISemaphoreDriver;

import java.util.List;
import java.util.Map;

public interface IDisplayController {
  void attachDisplay(IDisplayDriver displayDriver, ISemaphoreDriver semaphoreDriver);

  List<String> getDisplayList();

  Map<String, String> getDisplayData(String displayIp);

  void setDisplayData(Map<String, String> newDisplayData);
}
