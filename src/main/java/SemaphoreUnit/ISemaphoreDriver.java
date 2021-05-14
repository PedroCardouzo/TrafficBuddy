package SemaphoreUnit;

public interface ISemaphoreDriver {
  void close();

  void open();

  String getIp();

  String getDescription();

  void setDescription(String description);
}
