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
  private final ConfigurationManager confManager;
  private JPanel cardPanel;
  private JPanel addDeviceViewPanel;
  private JTextField textField1;
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
  private CardLayout cardLayout;
  private ButtonGroup group;

  public UserInterface() {
    group = new ButtonGroup();
    group.add(semaphore);
    group.add(speedRadar);
    group.add(display);

    this.confManager = new ConfigurationManager();

    addButton.addActionListener(actionEvent -> {
      final String ipAddress = textField1.getText();
      if (semaphore.isSelected()) {
        this.confManager.attachSemaphore(ipAddress);
      } else if (speedRadar.isSelected()) {
        this.confManager.attachSpeedRadar(ipAddress);
      } else if (display.isSelected()) {
        this.confManager.attachDisplay(ipAddress);
      }
    });

    addDevicesButton.addActionListener(actionEvent -> this.cardLayout.show(cardPanel, "addDevicePanel"));
    backButton1.addActionListener(actionEvent -> this.cardLayout.show(cardPanel, "mainMenuPanel"));
    backButton2.addActionListener(actionEvent -> this.cardLayout.show(cardPanel, "mainMenuPanel"));

    confSemaphoresButton.addActionListener(actionEvent -> {
      this.confManager.getSemaphoreList().forEach(semaphoresDropdown::addItem);
      this.cardLayout.show(cardPanel, "configureSemaphoresPanel");
    });

    modifySemaphore.addActionListener((actionEvent -> {
      Map<String, String> newSemaphoreData = new HashMap<>();
      newSemaphoreData.put(CustomConstants.IP_ADDRESS, ipAddress.getText());
      newSemaphoreData.put(CustomConstants.TRAFFIC_FLUX, trafficFlux.getText());
      newSemaphoreData.put(CustomConstants.CYCLE_PERIOD, cyclePeriod.getText());
      newSemaphoreData.put(CustomConstants.SEMAPHORE_DESCRIPTION, semDescription.getText());
      this.confManager.setSemaphoreData(newSemaphoreData);
    }));

    semaphoresDropdown.addActionListener(actionEvent -> {
      final Map<String, String> semaphoreData = this.confManager.getSemaphoreData(Objects.requireNonNull(semaphoresDropdown.getSelectedItem()).toString());
      ipAddress.setText(semaphoreData.getOrDefault(CustomConstants.IP_ADDRESS, ""));
      trafficFlux.setText(semaphoreData.getOrDefault(CustomConstants.TRAFFIC_FLUX, ""));
      cyclePeriod.setText(semaphoreData.getOrDefault(CustomConstants.CYCLE_PERIOD, ""));
      semDescription.setText(semaphoreData.getOrDefault(CustomConstants.SEMAPHORE_DESCRIPTION, ""));
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
