package SemaphoreUnit;

import java.util.Random;

public class TrafficCameraDriver {
  private final int upperbound;
  private Random rng;

  public TrafficCameraDriver(int upperbound) {
    this.rng = new Random();
    this.upperbound = upperbound;
  }

  public int getTraffixFlux() {
    return this.rng.nextInt(upperbound);
  }
}
