package SemaphoreUnit;

import DisplayUnit.IDisplayDriver;

public interface ISemaphoreDriver {
  void close();

  void open();

  String getIp();

  String getDescription();

  void setDescription(String description);

  void setFluxIntensityMessage(String fluxIntensity);

  String getFluxIntensityMessage();

  void attachDisplay(IDisplayDriver dd);
}
