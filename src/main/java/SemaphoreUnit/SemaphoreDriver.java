package SemaphoreUnit;

import DisplayUnit.IDisplayDriver;

import java.util.ArrayList;
import java.util.List;

public class SemaphoreDriver implements ISemaphoreDriver {
  private String description;
  private boolean open;
  private final String ip;
  private String fluxIntensityMessage;
  List<IDisplayDriver> displays;

  public SemaphoreDriver(final String ip, final String description) {
    this.open = false;
    this.ip = ip;
    this.description = description;
    this.displays = new ArrayList<>();
  }

  @Override
  public synchronized void close() {
    this.open = false;
    this.updateSemaphoreHardwareToClose();
  }

  private void updateSemaphoreHardwareToClose() {
    // dummy
  }

  @Override
  public synchronized void open() {
    this.open = true;
    this.updateSemaphoreHardwareToOpen();
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
  public void setDescription(final String description) {
    this.description = description;
  }

  @Override
  public void setFluxIntensityMessage(final String fluxIntensity) {
    this.fluxIntensityMessage = fluxIntensity;
    this.displays.forEach(d -> d.setMessage(fluxIntensityMessage));
  }

  @Override
  public String getFluxIntensityMessage() {
    return this.fluxIntensityMessage;
  }

  @Override
  public void attachDisplay(IDisplayDriver dd) {
    this.displays.add(dd);
  }

  private void updateSemaphoreHardwareToOpen() {
    // dummy
  }
}
