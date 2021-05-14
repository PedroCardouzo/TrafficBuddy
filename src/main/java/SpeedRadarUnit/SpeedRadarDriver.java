package SpeedRadarUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class SpeedRadarDriver implements ISpeedRadarDriver {

  private final int speedLimit;
  private final int speedLimitWithTolerance;
  private final String ip;

  public SpeedRadarDriver(final String ip, int speedLimit) {
    this.speedLimit = speedLimit;
    this.speedLimitWithTolerance = toleranceFunction(speedLimit);
    this.ip = ip;
  }

  private int toleranceFunction(int speedLimit) {
    return speedLimit >= 100 ? speedLimit + 7 : speedLimit + (int) ((float) 7/100 * speedLimit);
  }

  @Override
  public List<Infraction> getInfractions() {
    // dummy : simulates a call to an actual speed radar, which would have its registered infractions logged
    Random rng = ThreadLocalRandom.current();
    final int infractionNumber = rng.nextInt(5);
    List<Infraction> infractions = new ArrayList<>();

    for (int i=0; i < infractionNumber; i++) {
      infractions.add(new Infraction("VGhpcyBpcyBhIGR1bW15IHN0cmluZyBmb3IgdGVzdGluZyBwdXJwb3Nlcwo=",
              rng.nextInt(100) + this.speedLimitWithTolerance,
              this.speedLimit,
              this.speedLimitWithTolerance));
    }

    return infractions;
  }
}
