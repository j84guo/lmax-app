package lmaxapp;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import java.nio.ByteBuffer;


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
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();

        /* produce ByteBuffer in main thread (e.g. read from network) */
        RingBuffer<LongEvent> buffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(buffer);
        ByteBuffer bytes = ByteBuffer.allocate(8);
        long value = 0;
        while (true) {
            bytes.putLong(0, value++);
            producer.onData(bytes);
            Thread.sleep(1000);
        }
    }
}
