package SemaphoreUnit;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SemaphoreHistory {
  List<String> semaphoreLogs;

  public SemaphoreHistory() {
    this.semaphoreLogs = new ArrayList<>();
  }

  public void log(final String logEntry) {
    this.semaphoreLogs.add(Timestamp.from(Instant.now()).toString() + '\n' + logEntry);
  }

  public List<String> getSemaphoreLogs() {
    return semaphoreLogs;
  }
}
