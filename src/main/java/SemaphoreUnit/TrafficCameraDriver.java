package SemaphoreUnit;

import General.CustomConstants;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class TrafficCameraDriver implements ITrafficCameraDriver {
  private static final int DEFAULT_UPDATE_TIME_SECONDS = 10; // SECONDS
  private int currentFlux;
  private final String ip;

  public TrafficCameraDriver(final String ip) {
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
}
