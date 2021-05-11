package SemaphoreUnit;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class TrafficCameraDriver implements ITrafficCameraDriver {
  private static final int DEFAULT_UPDATE_TIME_SECONDS = 10; // SECONDS
  private final int upperbound;
  private int currentFlux;
  private Instant lastUpdate;

  public TrafficCameraDriver(int upperbound) {
    this.upperbound = upperbound;
    this.lastUpdate = Instant.EPOCH;
  }

  public int getTrafficFlux() {
    if (Instant.now().isAfter(this.lastUpdate.plusSeconds(DEFAULT_UPDATE_TIME_SECONDS))) {
      currentFlux = ThreadLocalRandom.current().nextInt(upperbound);
      this.lastUpdate = Instant.now();
    }

    return currentFlux;
  }
}
