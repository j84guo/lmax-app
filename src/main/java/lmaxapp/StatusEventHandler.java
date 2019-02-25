package lmaxapp;

import com.lmax.disruptor.EventHandler;


/* one per thread, consumes from RingBuffer */
public class StatusEventHandler implements EventHandler<StatusEvent> {
  private int _eventPartition;
  private int _numPartitions;

  public StatusEventHandler(int eventPartition, int numPartitions) {
    _eventPartition = eventPartition;
    _numPartitions = numPartitions;
  }

  public void onEvent(StatusEvent event, long sequence, boolean endOfBatch) {
    if (sequence % _numPartitions != _eventPartition) {
      return;
    }
    System.out.printf("[%s] sequence: %d\n", Thread.currentThread().getName(), sequence);
    System.out.println(event.status.getText());
  }
}
