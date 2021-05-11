package SemaphoreUnit;

import java.util.List;

public interface ISemaphoreHistory {
  void log(final String logEntry);

  List<String> getSemaphoreLogs();
}
