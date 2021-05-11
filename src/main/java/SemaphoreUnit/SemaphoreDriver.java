package SemaphoreUnit;

public class SemaphoreDriver {
  private boolean open;

  public SemaphoreDriver() {
    this.open = false;
  }

  public synchronized void close() {
    this.open = false;
    this.updateSemaphoreHardwareToClose();
  }

  private void updateSemaphoreHardwareToClose() {
    // dummy
  }

  public synchronized void open() {
    this.open = true;
    this.updateSemaphoreHardwareToOpen();
  }

  private void updateSemaphoreHardwareToOpen() {
    // dummy
  }
}
