package urss.server.worker.worker;

public class Worker {
  private static final long defaultDelay = 10000;//10 seconds
  private long delay;

  public Worker() {
    this(this.defaultDelay);
  }

  public Worker(long delay) {
    setDelay(delay);
  }

  public void refreshFeeds(Long id) {
    System.out.println("worker fired ! id: " + id);
  }

  public setDelay(long delay) {
    this.delay = delay;
  }

  public getDelay() {
    this.delay;
  }

  public String toString() {
    return ("delay: " + this.delay);
  }
}
