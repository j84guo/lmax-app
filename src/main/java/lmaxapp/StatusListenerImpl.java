package lmaxapp;

import com.lmax.disruptor.RingBuffer;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;


/* read statuses off network in Twitter4j thread, then publish them to RingBuffer */
public class StatusListenerImpl implements StatusListener {
  private RingBuffer<StatusEvent> _ringBuffer;

  public StatusListenerImpl(RingBuffer<StatusEvent> ringBuffer) {
    this._ringBuffer = ringBuffer;
  }

  /* status posted */
  public void onStatus(Status status) {
    _ringBuffer.publishEvent((event, sequence, Status) -> event.status = status, status);
  }

  /* client should delete the status */
  public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    System.out.println("Deletion notice");
  }

  /* client's query is too broad, some results were not returned */
  public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    System.out.println("Track limitation");
  }

  /* client should strip location information from the status */
  public void onScrubGeo(long userId, long upToStatusId) {
    System.out.println("Scrub geo");
  }

  /* client should consume messages fast else risk being disconnected */
  public void onStallWarning(StallWarning warning) {
    System.out.println("Stall warning");
  }

  /* something went wrong  */
  public void onException(Exception e) {
    e.printStackTrace();
  }
}
