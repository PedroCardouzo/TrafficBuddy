package General;

public class CustomConstants {
  public static final String IP_ADDRESS = "ipAddress";
  public static final String TRAFFIC_FLUX = "trafficFlux";
  public static final String CYCLE_PERIOD = "cyclePeriod";
  public static final String DEVICE_DESCRIPTION = "deviceDescription";
  public static final String SEMAPHORE_TIMING = "semTiming";

  public static final int DEFAULT_UPPER_BOUND = 60;
  public static final int DEFAULT_OPEN_TIMING = 45;
  public static final int DEFAULT_SPEED_LIMIT = 60;

  public static final String SPEED_LIMIT = "speedLimit";

  public static final String BASE_64_IMAGE_JSON = "base64Image";
  public static final String PLATE_JSON = "plate";
  public static final String SPEED_JSON = "speed";
  public static final String NOMINAL_MAX_SPEED_JSON = "nominalMaxSpeed";
  public static final String MAX_SPEED_WITH_TOLERANCE_JSON = "maxSpeedWithTolerance";
  public static final String LOCATION_DESCRIPTION_JSON = "locationDescription";
  public static final String ORIGIN_SPEED_RADAR_JSON = "originSpeedRadar";
  public static final String TIMESTAMP_JSON = "timestamp";

  // TODO make configurable
  public static final int CONF_REESTIMATION_INTERVAL = 5;
  public static final int DEFAULT_INFRACTION_HISTORY_POLL_PERIOD_SECONDS = 20;
}
