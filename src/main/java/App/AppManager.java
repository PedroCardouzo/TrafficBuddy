package App;

import DisplayUnit.DisplayController;
import DisplayUnit.DisplayDriver;
import DisplayUnit.IDisplayDriver;
import General.CustomConstants;
import SemaphoreUnit.ISemaphoreController;
import SemaphoreUnit.SemaphoreController;
import SemaphoreUnit.SemaphoreDriver;
import SemaphoreUnit.TrafficCameraDriver;
import SpeedRadarUnit.ISpeedRadarController;
import General.HistoryRecorder;
import SpeedRadarUnit.SpeedRadarController;
import SpeedRadarUnit.SpeedRadarDriver;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class AppManager {
  private final ISemaphoreController semaphoreController;
  private final ISpeedRadarController speedRadarController;
  private final DisplayController displayController;

  public AppManager() {
    this.semaphoreController = new SemaphoreController(new HistoryRecorder());
    this.speedRadarController = new SpeedRadarController(new HistoryRecorder());
    this.displayController = new DisplayController(new HistoryRecorder());
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

  public void attachDisplay(final String ipAddress, final String description, final String semaphoreIpAddress) {
    IDisplayDriver dd = new DisplayDriver(ipAddress, description);
    this.displayController.attachDisplay(dd);
    this.semaphoreController.attachDisplay(semaphoreIpAddress, dd);
  }

  public Map<String, String> getSpeedRadarData(final String selectedSpeedRadar) {
    return this.speedRadarController.getSpeedRadarData(selectedSpeedRadar);
  }

  public void processInfractions() {
    List<JSONObject> infractionHistory = this.speedRadarController.collectInfractionHistory();
    infractionHistory.forEach(System.out::println);
    
    this.speedRadarController.clearInfractionHistory();
  }

  public void setSpeedRadarData(final Map<String, String> newSpeedRadarData) {
    this.speedRadarController.setSpeedRadarData(newSpeedRadarData);
  }

  public void start() {
    this.speedRadarController.start();
    this.semaphoreController.start();
  }

  public void stop() {
    this.speedRadarController.stop();
    this.semaphoreController.stop();
  }

  public List<String> getSpeedRadarList() {
    return this.speedRadarController.getSpeedRadarList();
  }

  public List<String> getDisplayList() {
    return this.displayController.getDisplayList();
  }

  public Map<String, String> getDisplayData(final String displayIp) {
    return this.displayController.getDisplayData(displayIp);
  }

  public void setDisplayData(final Map<String, String> newDisplayData) {
    this.displayController.setDisplayData(newDisplayData);
  }
}
