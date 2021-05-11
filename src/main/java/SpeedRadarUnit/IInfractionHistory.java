package SpeedRadarUnit;

import java.util.List;

public interface IInfractionHistory {
  void log(final String logEntry);

  List<String> getLogs();

  void clear();
}
