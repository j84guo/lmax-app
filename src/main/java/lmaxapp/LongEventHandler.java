package lmaxapp;

import com.lmax.disruptor.EventHandler;


/* Handle LongEvent objects. Each handler thread will receive all events but may choose which to handle based on the
   event's sequence number */
public class LongEventHandler implements EventHandler<LongEvent> {
  public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
    System.out.printf("[%s] event: %d\n", Thread.currentThread().getName(), sequence);
  }
}
