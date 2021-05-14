package SemaphoreUnit;

import java.util.List;
import java.util.Map;

public interface ISemaphoreController {
    Map<String, String> getSemaphoreData(final String target);

  void setSemaphoreData(Map<String, String> newSemaphoreData);

  List<String> getSemaphoreList();

  void attachSemaphore(final ISemaphoreDriver sd, final ITrafficCameraDriver tcd);

  }
