package SemaphoreUnit;

public interface ITrafficCameraDriver {
  int getTrafficFlux();

  void setTrafficFlux(final int trafficFlux);

  void setFluxIntensityMessage(String fluxIntensity);

  String getFluxIntensityMessage();
}
