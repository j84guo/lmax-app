package lmaxsample;


/* Message passed between threads */
public class LongEvent {
  public long value;

  public void set(long value) {
    this.value = value;
  }
}
