package SpeedRadarUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SpeedRadarDriver implements ISpeedRadarDriver {

  private int speedLimit;
  private int speedLimitWithTolerance;
  private final String ip;
  private String description;

  public SpeedRadarDriver(final String ip, final String description, int speedLimit) {
    this.ip = ip;
    this.description = description;
    this.setSpeedLimit(speedLimit);
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
              this.speedLimitWithTolerance,
              this.ip,
              this.description));
    }

    return infractions;
  }

  @Override
  public String getIp() {
    return this.ip;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public int getSpeedLimit() {
    return this.speedLimit;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void setSpeedLimit(int newSpeedLimit) {
    this.speedLimit = newSpeedLimit;
    this.speedLimitWithTolerance = toleranceFunction(newSpeedLimit);
  }
}
