package SemaphoreUnit;

import java.util.Random;

public class TrafficCameraDriver implements ITrafficCameraDriver {
  private final int upperbound;
  private Random rng;

  public TrafficCameraDriver(int upperbound) {
    this.rng = new Random();
    this.upperbound = upperbound;
  }

  public int getTrafficFlux() {
    return this.rng.nextInt(upperbound);
  }
}
