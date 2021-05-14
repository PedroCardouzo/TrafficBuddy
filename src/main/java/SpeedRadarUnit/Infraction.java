package SpeedRadarUnit;

import General.CustomConstants;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Infraction {
  private final String base64Image;
  private final String plate;
  private final int speed;
  private final int nominalMaxSpeed;
  private final int maxSpeedWithTolerance;
  private final String timestamp;
  private final String originLocationDescription;
  private final String originSpeedRadarIp;

  public Infraction(final String base64Image, final int speed, final int nominalMaxSpeed, final int maxSpeedWithTolerance, final String originSpeedRadarIp, final String originLocationDescription) {
    this.base64Image = base64Image;
    this.plate = Infraction.extractPlateNumberFromBase64Image(base64Image);
    this.speed = speed;
    this.nominalMaxSpeed = nominalMaxSpeed;
    this.maxSpeedWithTolerance = maxSpeedWithTolerance;
    this.timestamp = Timestamp.from(Instant.now().minusSeconds(ThreadLocalRandom.current().nextInt(10000))).toString(); // for some variance
    this.originLocationDescription = originLocationDescription;
    this.originSpeedRadarIp = originSpeedRadarIp;
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

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put(CustomConstants.BASE_64_IMAGE_JSON, this.base64Image);
    json.put(CustomConstants.PLATE_JSON, this.plate);
    json.put(CustomConstants.SPEED_JSON, this.speed);
    json.put(CustomConstants.NOMINAL_MAX_SPEED_JSON, this.nominalMaxSpeed);
    json.put(CustomConstants.MAX_SPEED_WITH_TOLERANCE_JSON, this.maxSpeedWithTolerance);
    json.put(CustomConstants.LOCATION_DESCRIPTION_JSON, this.originLocationDescription);
    json.put(CustomConstants.ORIGIN_SPEED_RADAR_JSON, this.originSpeedRadarIp);
    json.put(CustomConstants.TIMESTAMP_JSON, this.timestamp);
    return json;
  }
}
