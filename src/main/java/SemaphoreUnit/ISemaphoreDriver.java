package SemaphoreUnit;

import DisplayUnit.IDisplayDriver;

public interface ISemaphoreDriver {
  void close();

  void open();

  String getIp();

  String getDescription();

  void setDescription(String description);
}
