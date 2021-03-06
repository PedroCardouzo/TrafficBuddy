package DisplayUnit;

import General.CustomConstants;
import General.IHistoryRecorder;

import java.util.ArrayList;;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DisplayController implements IDisplayController {
  private final IHistoryRecorder displayHistory;
  private List<IDisplayDriver> displays;


  public DisplayController(final IHistoryRecorder displayHistory) {
    this.displays = new ArrayList<>();
    this.displayHistory = displayHistory;
  }

  @Override
  public void attachDisplay(final IDisplayDriver displayDriver) {
    this.displays.add(displayDriver);
  }

  @Override
  public List<String> getDisplayList() {
    return this.displays.stream().map(IDisplayDriver::getIp).collect(Collectors.toList());
  }

  @Override
  public Map<String, String> getDisplayData(String displayIp) {
    for (final IDisplayDriver display : displays) {
      if (display.getIp().equals(displayIp)) {
        Map<String, String> newDisplayData = new HashMap<>();
        newDisplayData.put(CustomConstants.IP_ADDRESS, display.getIp());
        newDisplayData.put(CustomConstants.DEVICE_DESCRIPTION, display.getDescription());
        newDisplayData.put(CustomConstants.DISPLAY_MESSAGE, String.valueOf(display.getMessage()));
        newDisplayData.put(CustomConstants.DISPLAY_MANUAL_MODE, String.valueOf(display.isManualMode()));
        return newDisplayData;
      }
    }

    return Collections.emptyMap();
  }

  @Override
  public void setDisplayData(final Map<String, String> newDisplayData) {
    final String ip = newDisplayData.getOrDefault("ipAddress", "");
    IDisplayDriver display = displays.stream().filter((IDisplayDriver x) -> x.getIp().equals(ip)).findFirst().orElse(null);

    if (display != null) {
      display.setDescription(newDisplayData.getOrDefault(CustomConstants.DEVICE_DESCRIPTION, display.getDescription()));
      final boolean manualMode = Boolean.parseBoolean(newDisplayData.getOrDefault(CustomConstants.DISPLAY_MANUAL_MODE, "false"));
      final String msg = newDisplayData.put(CustomConstants.DISPLAY_MESSAGE, display.getMessage());

      if (manualMode) {
        display.setManualMessage(msg);
      } else {
        display.exitManualMode();
        display.setMessage(msg);
      }

      this.displayHistory.log(newDisplayData);
    }
  }
}
