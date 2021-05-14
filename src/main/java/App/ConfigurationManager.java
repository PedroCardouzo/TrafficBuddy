package App;

import General.CustomConstants;
import SemaphoreUnit.ISemaphoreController;
import SemaphoreUnit.SemaphoreController;
import SemaphoreUnit.SemaphoreDriver;
import SemaphoreUnit.SemaphoreHistory;
import SemaphoreUnit.TrafficCameraDriver;
import SpeedRadarUnit.ISpeedRadarController;
import SpeedRadarUnit.InfractionHistory;
import SpeedRadarUnit.SpeedRadarController;
import SpeedRadarUnit.SpeedRadarDriver;

import java.util.List;
import java.util.Map;

public class ConfigurationManager {
  private final ISemaphoreController semaphoreController;
  private final ISpeedRadarController speedRadarController;

  public ConfigurationManager() {
    this.semaphoreController = new SemaphoreController(new SemaphoreHistory());
    this.speedRadarController = new SpeedRadarController(new InfractionHistory(), CustomConstants.DEFAULT_INFRACTION_HISTORY_POLL_PERIOD_SECONDS);
  }

  public void setSemaphoreData(Map<String, String> newSemaphoreData) {
    this.semaphoreController.setSemaphoreData(newSemaphoreData);
  }

  public Map<String, String> getSemaphoreData(final String selectedItem) {
    return this.semaphoreController.getSemaphoreData(selectedItem);
  }

  public List<String> getSemaphoreList() {
    return this.semaphoreController.getSemaphoreList();
  }

  public void attachSemaphore(final String ipAddress) {
    this.semaphoreController.attachSemaphore(new SemaphoreDriver(ipAddress), new TrafficCameraDriver(ipAddress));
  }

  public void attachSpeedRadar(final String ipAddress) {
    this.speedRadarController.attachSpeedRadar(new SpeedRadarDriver(ipAddress, CustomConstants.DEFAULT_SPEED_LIMIT));
  }

  public void attachDisplay(final String ipAddress) {
    // TODO implement
  }
}
