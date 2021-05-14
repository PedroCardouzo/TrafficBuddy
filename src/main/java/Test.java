import App.UserInterface;

import javax.swing.*;

public class Test {
  private static final int SEMAPHORE_NUMBER = 4;
  private static final int SPEEDRADAR_NUMBER = 3;

  public static void main(String[] args) {
    UserInterface ui = new UserInterface();
    ui.run();

//    final SemaphoreController semaphoreController = new SemaphoreController(new SemaphoreHistory());
//    final SpeedRadarController speedRadarController = new SpeedRadarController(new InfractionHistory(), 29);
//
//    for (int i=0; i < SEMAPHORE_NUMBER; i++) {
//      semaphoreController.attachSemaphore(new SemaphoreDriver("192.168.1.1"), new TrafficCameraDriver("192.168.1.1", 30), 30);
//    }
//
//    for (int i=0; i < SPEEDRADAR_NUMBER; i++) {
//      speedRadarController.attachSpeedRadar(new SpeedRadarDriver("192.168.1.2", ThreadLocalRandom.current().nextInt(120)));
//    }
//
//    semaphoreController.start();
//    speedRadarController.start();
//
//    while (true) {
//      System.out.println("\n======================================");
//
//      System.out.println("Semaphore States");
//      for (final String log : semaphoreController.getHistory()) {
//        System.out.println(log);
//      }
//
//      System.out.println("Infractions");
//      for (final String log : speedRadarController.collectInfractionHistory()) {
//        System.out.println(log);
//      }
//
//      try {
//        Thread.sleep(31000); // milliseconds
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    }
  }
}
