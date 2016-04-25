/**
 * Created by uesu on 12/4/16.
 */


import java.util.concurrent.*;

public class bidAskTable {
    private byte bb;

    private static final long serialVersionUID = 1L;
    // Instance attributes used in this example

    public static void main( String args[] ) throws Exception {
        BlockingQueue queue = new ArrayBlockingQueue(1024);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
