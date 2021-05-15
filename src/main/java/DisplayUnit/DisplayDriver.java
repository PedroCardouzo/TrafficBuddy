package DisplayUnit;

public class DisplayDriver implements IDisplayDriver {
  private final String ipAddress;
  private String description;
  private String message;
  private boolean manualMode;

  public DisplayDriver(final String ipAddress, final String description) {
    this.ipAddress = ipAddress;
    this.description = description;
    this.manualMode = false;
    this.message = "";
  }

  @Override
  public void setMessage(final String message) {
    if (!manualMode)
      this.message = message;
  }

  @Override
  public void setManualMessage(String message) {
    this.manualMode = true;
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public void clearManualMessage() {
    this.manualMode = false;
  }

  @Override
  public String getIp() {
    return this.ipAddress;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public boolean isManualMode() {
    return this.manualMode;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }
}
