package lmaxapp;


/* Message passed between threads */
public class LongEvent {
  private long value;

  public void set(long value) {
    this.value = value;
  }
}
