package App;

import General.CustomConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserInterface {
  private final AppManager appManager;
  private JPanel cardPanel;
  private JPanel addDeviceViewPanel;
  private JTextField deviceIpTextField;
  private JRadioButton semaphore;
  private JRadioButton speedRadar;
  private JRadioButton display;
  private JButton addButton;
  private JPanel mainMenu;
  private JButton confSpeedRadarButton;
  private JButton confSemaphoresButton;
  private JButton addDevicesButton;
  private JButton confDisplaysButton;
  private JButton backButton1;
  private JPanel configureSemaphoresPanel;
  private JComboBox semaphoresDropdown;
  private JTextField ipAddress;
  private JTextField trafficFlux;
  private JTextField cyclePeriod;
  private JButton backButton2;
  private JButton modifySemaphore;
  private JTextField semDescription;
  private JTextField descriptionTextField;
  private JLabel openTimingLabel;
  private JComboBox speedRadarDropdown;
  private JTextField ipAddressSRText;
  private JTextField speedLimitText;
  private JTextField descriptionSRText;
  private JPanel configureSpeedRadarPanel;
  private JButton infractionCommitButton;
  private JButton modifySpeedRadarButton;
  private JButton backButton3;
  private JTextField reestimationTiming;
  private JTextField infractionPollPeriodText;
  private JButton startStop;
  private JComboBox selectionSemaphoreDropdown;
  private JComboBox displayDropdown;
  private JCheckBox manualMessageCheckbox;
  private JTextField ipAddressDispText;
  private JTextField messageDispText;
  private JTextField descriptionDispText;
  private JPanel configureDisplayPanel;
  private JButton backButton4;
  private JButton modifyDisplayButton;
  private CardLayout cardLayout;
  private ButtonGroup group;

  public UserInterface() {
    group = new ButtonGroup();
    group.add(semaphore);
    group.add(speedRadar);
    group.add(display);

    this.appManager = new AppManager();

    addButton.addActionListener(actionEvent -> {
      final String ipAddress = deviceIpTextField.getText();
      final String description = descriptionTextField.getText();

      if (semaphore.isSelected()) {
        this.appManager.attachSemaphore(ipAddress, description);
        selectionSemaphoreDropdown.removeAllItems();
        this.appManager.getSemaphoreList().forEach(selectionSemaphoreDropdown::addItem);
      } else if (speedRadar.isSelected()) {
        this.appManager.attachSpeedRadar(ipAddress, description);
      } else if (display.isSelected()) {
        final String selectedSemaphoreForDisplay = Objects.requireNonNull(selectionSemaphoreDropdown.getSelectedItem()).toString();
        this.appManager.attachDisplay(ipAddress, description, selectedSemaphoreForDisplay);
      }
    });

    addDevicesButton.addActionListener(actionEvent -> {
      selectionSemaphoreDropdown.removeAllItems();
      this.appManager.getSemaphoreList().forEach(selectionSemaphoreDropdown::addItem);
      this.cardLayout.show(cardPanel, "addDevicePanel");
    });

    backButton1.addActionListener(actionEvent -> this.cardLayout.show(cardPanel, "mainMenuPanel"));
    backButton2.addActionListener(actionEvent -> this.cardLayout.show(cardPanel, "mainMenuPanel"));
    backButton3.addActionListener(actionEvent -> this.cardLayout.show(cardPanel, "mainMenuPanel"));
    backButton4.addActionListener(actionEvent -> this.cardLayout.show(cardPanel, "mainMenuPanel"));

    confSemaphoresButton.addActionListener(actionEvent -> {
      semaphoresDropdown.removeAllItems();
      this.appManager.getSemaphoreList().forEach(semaphoresDropdown::addItem);
      this.cardLayout.show(cardPanel, "configureSemaphoresPanel");
    });

    confSpeedRadarButton.addActionListener(actionEvent -> {
      speedRadarDropdown.removeAllItems();
      this.appManager.getSpeedRadarList().forEach(speedRadarDropdown::addItem);
      this.cardLayout.show(cardPanel, "configureSpeedRadarPanel");
    });

    confDisplaysButton.addActionListener(actionEvent -> {
      displayDropdown.removeAllItems();
      this.appManager.getDisplayList().forEach(displayDropdown::addItem);
      this.cardLayout.show(cardPanel, "configureDisplayPanel");
    });

    modifySemaphore.addActionListener((actionEvent -> {
      Map<String, String> newSemaphoreData = new HashMap<>();
      newSemaphoreData.put(CustomConstants.IP_ADDRESS, ipAddress.getText());
      newSemaphoreData.put(CustomConstants.TRAFFIC_FLUX, trafficFlux.getText());
      newSemaphoreData.put(CustomConstants.CYCLE_PERIOD, cyclePeriod.getText());
      newSemaphoreData.put(CustomConstants.DEVICE_DESCRIPTION, semDescription.getText());
      newSemaphoreData.put(CustomConstants.CONF_REESTIMATION_INTERVAL_JSON, reestimationTiming.getText());
      this.appManager.setSemaphoreData(newSemaphoreData);
    }));

    semaphoresDropdown.addActionListener(actionEvent -> {
      final Object selectedItem = semaphoresDropdown.getSelectedItem();

      if (selectedItem != null) {
        final Map<String, String> semaphoreData = this.appManager.getSemaphoreData(selectedItem.toString());
        ipAddress.setText(semaphoreData.getOrDefault(CustomConstants.IP_ADDRESS, ""));
        trafficFlux.setText(semaphoreData.getOrDefault(CustomConstants.TRAFFIC_FLUX, ""));
        cyclePeriod.setText(semaphoreData.getOrDefault(CustomConstants.CYCLE_PERIOD, ""));
        semDescription.setText(semaphoreData.getOrDefault(CustomConstants.DEVICE_DESCRIPTION, ""));
        openTimingLabel.setText(semaphoreData.getOrDefault(CustomConstants.SEMAPHORE_TIMING, ""));
        reestimationTiming.setText(semaphoreData.getOrDefault(CustomConstants.CONF_REESTIMATION_INTERVAL_JSON, ""));
      }
    });

    speedRadarDropdown.addActionListener(actionEvent -> {
      final Object selectedItem = speedRadarDropdown.getSelectedItem();

      if (selectedItem != null) {
        final Map<String, String> semaphoreData = this.appManager.getSpeedRadarData(selectedItem.toString());
        ipAddressSRText.setText(semaphoreData.getOrDefault(CustomConstants.IP_ADDRESS, ""));
        speedLimitText.setText(semaphoreData.getOrDefault(CustomConstants.SPEED_LIMIT, ""));
        descriptionSRText.setText(semaphoreData.getOrDefault(CustomConstants.DEVICE_DESCRIPTION, ""));
        infractionPollPeriodText.setText(semaphoreData.getOrDefault(CustomConstants.DEFAULT_INFRACTION_HISTORY_POLL_PERIOD_SECONDS_JSON, ""));
      }
    });

    infractionCommitButton.addActionListener(actionEvent -> this.appManager.processInfractions());

    modifySpeedRadarButton.addActionListener(actionEvent -> {
      Map<String, String> newSpeedRadar = new HashMap<>();
      newSpeedRadar.put(CustomConstants.IP_ADDRESS, ipAddressSRText.getText());
      newSpeedRadar.put(CustomConstants.DEVICE_DESCRIPTION, descriptionSRText.getText());
      newSpeedRadar.put(CustomConstants.SPEED_LIMIT, speedLimitText.getText());
      newSpeedRadar.put(CustomConstants.DEFAULT_INFRACTION_HISTORY_POLL_PERIOD_SECONDS_JSON, infractionPollPeriodText.getText());
      this.appManager.setSpeedRadarData(newSpeedRadar);
    });

    startStop.addActionListener(actionEvent -> {
      final String currentMode = startStop.getText();
      final String nextMode = currentMode.equals(CustomConstants.START_TEXT) ? CustomConstants.STOP_TEXT : CustomConstants.START_TEXT;
      startStop.setText(nextMode); // flip

      if (currentMode.equals(CustomConstants.START_TEXT)) {
        this.appManager.start();
      } else {
        this.appManager.stop();
      }
    });

    displayDropdown.addActionListener(actionEvent -> {
      final Object selectedItem = displayDropdown.getSelectedItem();

      if (selectedItem != null) {
        final Map<String, String> displayData = this.appManager.getDisplayData(selectedItem.toString());
        ipAddressDispText.setText(displayData.getOrDefault(CustomConstants.IP_ADDRESS, ""));
        messageDispText.setText(displayData.getOrDefault(CustomConstants.DISPLAY_MESSAGE, ""));
        manualMessageCheckbox.setSelected(Boolean.parseBoolean(displayData.getOrDefault(CustomConstants.DISPLAY_MANUAL_MODE, "false")));
        descriptionDispText.setText(displayData.getOrDefault(CustomConstants.DEVICE_DESCRIPTION, ""));
      }

      this.appManager.getDisplayList().forEach(speedRadarDropdown::addItem);
    });

    manualMessageCheckbox.addActionListener(actionEvent -> messageDispText.setEditable(manualMessageCheckbox.isSelected()));

    modifyDisplayButton.addActionListener(actionEvent -> {
      Map<String, String> newDisplayData = new HashMap<>();
      newDisplayData.put(CustomConstants.IP_ADDRESS, ipAddressDispText.getText());
      newDisplayData.put(CustomConstants.DISPLAY_MESSAGE, messageDispText.getText());
      newDisplayData.put(CustomConstants.DISPLAY_MANUAL_MODE, String.valueOf(manualMessageCheckbox.isSelected()));
      newDisplayData.put(CustomConstants.DEVICE_DESCRIPTION, descriptionDispText.getText());
      this.appManager.setDisplayData(newDisplayData);
    });
  }

  public void run() {
    JFrame frame = new JFrame();
    frame.setContentPane(cardPanel);
    this.cardLayout = (CardLayout) cardPanel.getLayout();
    this.cardLayout.show(cardPanel, "mainMenuPanel");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
