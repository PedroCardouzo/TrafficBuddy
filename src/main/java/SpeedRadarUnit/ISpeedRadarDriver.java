package SpeedRadarUnit;

import java.util.List;

public interface ISpeedRadarDriver {
  List<Infraction> getInfractions();

  String getIp();

  String getDescription();

  int getSpeedLimit();

  void setDescription(String description);

  void setSpeedLimit(final int newSpeedLimit);
}
