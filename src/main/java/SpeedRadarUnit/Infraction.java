package SpeedRadarUnit;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Infraction {
  private String base64Image;
  private String plate;
  private int speed;
  private int nominalMaxSpeed;
  private int maxSpeedWithTolerance;
  private String timestamp;

  public Infraction(final String base64Image, final int speed, final int nominalMaxSpeed, final int maxSpeedWithTolerance) {
    this.base64Image = base64Image;
    this.plate = Infraction.extractPlateNumberFromBase64Image(base64Image);
    this.speed = speed;
    this.nominalMaxSpeed = nominalMaxSpeed;
    this.maxSpeedWithTolerance = maxSpeedWithTolerance;
    this.timestamp = Timestamp.from(Instant.now().minusSeconds(ThreadLocalRandom.current().nextInt(10000))).toString(); // for some variance
  }

  private static String extractPlateNumberFromBase64Image(String base64Image) {
    final StringBuilder plateBuilder = new StringBuilder();
    Random r = ThreadLocalRandom.current();
    plateBuilder.append((char)(r.nextInt(26) + 'A'));
    plateBuilder.append((char)(r.nextInt(26) + 'A'));
    plateBuilder.append((char)(r.nextInt(26) + 'A'));
    plateBuilder.append('-');
    plateBuilder.append(r.nextInt(10));
    plateBuilder.append(r.nextInt(10));
    plateBuilder.append(r.nextInt(10));
    plateBuilder.append(r.nextInt(10));
    return plateBuilder.toString();
  }

  @Override
  public String toString() {
    JSONObject json = new JSONObject();
    json.put("base64Image", this.base64Image);
    json.put("plate", this.plate);
    json.put("speed", this.speed);
    json.put("nominalMaxSpeed", this.nominalMaxSpeed);
    json.put("maxSpeedWithTolerance", this.maxSpeedWithTolerance);
    json.put("timestamp", this.timestamp);
    return json.toString();
  }
}
