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
import org.json.JSONObject;

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

  public Map<String, String> getSemaphoreData(final String selectedSemaphore) {
    return this.semaphoreController.getSemaphoreData(selectedSemaphore);
  }

  public List<String> getSemaphoreList() {
    return this.semaphoreController.getSemaphoreList();
  }

  public void attachSemaphore(final String ipAddress, final String description) {
    this.semaphoreController.attachSemaphore(new SemaphoreDriver(ipAddress, description), new TrafficCameraDriver(ipAddress));
  }

  public void attachSpeedRadar(final String ipAddress, final String description) {
    this.speedRadarController.attachSpeedRadar(new SpeedRadarDriver(ipAddress, description, CustomConstants.DEFAULT_SPEED_LIMIT));
  }

  public void attachDisplay(final String ipAddress, final String description) {
    // TODO implement
  }

  public Map<String, String> getSpeedRadarData(final String selectedSpeedRadar) {
    return this.speedRadarController.getSpeedRadarData(selectedSpeedRadar);
  }

  public void processInfractions() {
    List<JSONObject> infractionHistory = this.speedRadarController.collectInfractionHistory();
    infractionHistory.forEach(System.out::println);
    // TODO database store here
    // after
    this.speedRadarController.clearInfractionHistory();
  }
}
