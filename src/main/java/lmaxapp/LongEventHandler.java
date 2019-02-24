package lmaxapp;

import com.lmax.disruptor.EventHandler;


/* Handle LongEvents objects */
public class LongEventHandler implements EventHandler<LongEvent> {
  public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
    System.out.printf("event: %d\n", sequence);
  }
}
