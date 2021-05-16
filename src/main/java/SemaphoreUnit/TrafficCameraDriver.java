package SemaphoreUnit;

import General.CustomConstants;
import General.ObservableSubject;

import java.util.concurrent.ThreadLocalRandom;

public class TrafficCameraDriver extends ObservableSubject implements ITrafficCameraDriver {
  private static final int DEFAULT_UPDATE_TIME_SECONDS = 10; // SECONDS
  private int currentFlux;
  private final String ip;
  private String fluxIntensityMessage;

  public TrafficCameraDriver(final String ip) {
    super();
    this.ip = ip;
    this.currentFlux = ThreadLocalRandom.current().nextInt(CustomConstants.DEFAULT_UPPER_BOUND);
  }

  @Override
  public int getTrafficFlux() {
    return currentFlux;
  }

  @Override
  public void setTrafficFlux(final int trafficFlux) {
    this.currentFlux = trafficFlux;
  }

  @Override
  public void setFluxIntensityMessage(final String fluxIntensity) {
    if (this.fluxIntensityMessage != null && this.fluxIntensityMessage.equals(fluxIntensity))
      return;

    this.fluxIntensityMessage = fluxIntensity;
    this.notifyObservers(fluxIntensity);
  }

  @Override
  public String getFluxIntensityMessage() {
    return this.fluxIntensityMessage;
  }
}
