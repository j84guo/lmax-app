package lmaxapp;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import twitter4j.TwitterStreamFactory;


public class App {
  /* partition events modulo the number of handlers */
  private static StatusEventHandler[] initEventHandlers(int numPartitions) {
    StatusEventHandler[] handlers = new StatusEventHandler[numPartitions];
    for (int i=0; i<numPartitions; ++i) {
      handlers[i] = new StatusEventHandler(i, numPartitions);
    }
    return handlers;
  }

  /* event factory pre-allocates space
     ring buffer size is power of 2 so that modulo operation may be done using a bitmask
     one daemon thread per handler
     single producer uses no locks
     blocking wait strategy uses an underlying condition variable */
  private static Disruptor<StatusEvent> initDisruptor() {
    Disruptor<StatusEvent> disruptor = new Disruptor<>(
        StatusEvent::new,
        1024,
        DaemonThreadFactory.INSTANCE,
        ProducerType.SINGLE,
        new BlockingWaitStrategy());
    
    disruptor.handleEventsWith(
        initEventHandlers(2));
    return disruptor;
  }

  /* Twitter4j (starts a network thread) to Disruptor (starts a pool of consumer threads) */
  private static void processTweets() {
    Disruptor<StatusEvent> disruptor = initDisruptor();
    disruptor.start();

    TwitterStreamFactory.getSingleton()
        .addListener(new StatusListenerImpl(disruptor.getRingBuffer()))
        .sample();
  }

  public static void main(String[] args) {
    try {
      processTweets();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
