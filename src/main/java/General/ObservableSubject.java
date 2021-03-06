package General;

import java.util.ArrayList;
import java.util.List;

public abstract class ObservableSubject {
  private final List<IObserver> observerList;

  public ObservableSubject() {
    this.observerList = new ArrayList<>();
  }

  public void attach(final IObserver observer) {
    this.observerList.add(observer);
  }

  public void detach(final int index) {
    this.observerList.remove(index);
  }

  public void notifyObservers(final String data) {
    observerList.forEach(observer -> observer.update(data));
  }
}
