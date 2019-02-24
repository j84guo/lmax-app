package lmaxapp;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.nio.ByteBuffer;


/* Sequence numbers:
   In the disruptor pattern, a RingBuffer tracks its next sequence number. Each consumer also tracks its own sequence
   number. Since a sequence number is updated only by its owner, an important source of contention is removed

   Reading:
   Consumers ask what the highest sequence number is, then consume up to it without contention, since the writer is not
   writing to those spots

   Writing:
   Phase 1 - Publisher determines the next available spot. RingBuffer determines if next slot is free by checking the
             sequences of EventProcessors reading from it
   Phase 2 - Write into slot, commit (causes RingBuffer to update sequence number) */
public class App {
    public static void main(String[] args) throws InterruptedException {
        /* performance improves by specifying explicitly when we have a single writing thread */
        Disruptor<LongEvent> disruptor = new Disruptor<>(
            new LongEventFactory(),
            1024,
            DaemonThreadFactory.INSTANCE,
            ProducerType.SINGLE,
            new BlockingWaitStrategy());

        /* single consumer thread */
        disruptor.handleEventsWith(new LongEventHandler(), new LongEventHandler());
        disruptor.start();

        /* produce ByteBuffer in main thread (e.g. read from network) */
        LongEventProducer producer = new LongEventProducer(disruptor.getRingBuffer());
        ByteBuffer bytes = ByteBuffer.allocate(8);
        long value = 0;
        while (true) {
            bytes.putLong(0, value++);
            producer.onData(bytes);
            Thread.sleep(1000);
        }
    }
}
