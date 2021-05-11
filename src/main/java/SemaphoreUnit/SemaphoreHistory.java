package SemaphoreUnit;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SemaphoreHistory implements ISemaphoreHistory {
  List<String> semaphoreLogs;

  public SemaphoreHistory() {
    this.semaphoreLogs = new ArrayList<>();
  }

  public void log(final String logEntry) {
    this.semaphoreLogs.add(Timestamp.from(Instant.now()).toString() + '\n' + logEntry);
  }

  public List<String> getLogs() {
    return semaphoreLogs;
  }

  public static ISemaphoreHistory getLogger() {
    return new SemaphoreHistory();
  }
}
