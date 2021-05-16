package SemaphoreUnit;

import DisplayUnit.IDisplayDriver;

import java.util.ArrayList;
import java.util.List;

public class SemaphoreDriver implements ISemaphoreDriver {
  private String description;
  private boolean open;
  private final String ip;
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

  private void updateSemaphoreHardwareToOpen() {
    // dummy
  }
}
