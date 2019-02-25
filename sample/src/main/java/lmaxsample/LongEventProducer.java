package lmaxsample;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import java.nio.ByteBuffer;


/* Translate binary into event object and publish into ring buffer */
public class LongEventProducer {
  private final RingBuffer<LongEvent> buffer;

  public LongEventProducer(RingBuffer buffer) {
    this.buffer = buffer;
  }

  private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR =
      (event, sequence, bytes) -> event.set(bytes.getLong(0));

  public void onData(ByteBuffer bytes) {
    buffer.publishEvent(TRANSLATOR, bytes);
  }
}
