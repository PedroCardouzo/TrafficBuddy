import SemaphoreUnit.SemaphoreController;
import SemaphoreUnit.SemaphoreDriver;
import SemaphoreUnit.SemaphoreHistory;
import SemaphoreUnit.TrafficCameraDriver;

import java.net.MalformedURLException;

public class Test {
  private static final int SEMAPHORE_NUMBER = 4;

  public static void main(String[] args) {
    final SemaphoreController semaphoreController = new SemaphoreController(new SemaphoreHistory());
    for (int i=0; i < SEMAPHORE_NUMBER; i++) {
      semaphoreController.attachSemaphore(new SemaphoreDriver(), new TrafficCameraDriver(30), 30);
    }

    semaphoreController.start();

    while (true) {
      System.out.println("Logging");
      for (final String log : semaphoreController.getHistory()) {
        System.out.println(log);
      }

      try {
        Thread.sleep(31000); // milliseconds
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
