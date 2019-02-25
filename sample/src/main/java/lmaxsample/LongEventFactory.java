package lmaxsample;

import com.lmax.disruptor.EventFactory;


/* Allow Disruptor to pre-allocate events */
public class LongEventFactory implements EventFactory<LongEvent> {
  public LongEvent newInstance() {
    return new LongEvent();
  }
}
