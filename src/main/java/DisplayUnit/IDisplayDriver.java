package DisplayUnit;

public interface IDisplayDriver {
  void setMessage(String message);

  void setManualMessage(String message);

  String getMessage();

  void exitManualMode();

  String getIp();

  String getDescription();

  boolean isManualMode();

  void setDescription(String description);
}
