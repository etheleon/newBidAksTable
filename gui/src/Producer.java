import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.BlockingQueue;

/**
 * Created by uesu on 13/4/16.
 */

public class Producer implements Runnable {

    protected BlockingQueue queue = null;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }
    public void run() {
        try {
            InetAddress group = InetAddress.getByName("224.5.6.7");
            MulticastSocket s = new MulticastSocket(1234);
            s.joinGroup(group);

            byte[] buf = new byte[4000];
            while(true)
            {
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                s.receive(recv);
                ByteBuffer b = ByteBuffer.wrap(recv.getData());
                b.order(ByteOrder.LITTLE_ENDIAN);
                queue.put(b);
            }
        } catch (InterruptedException e) {
            e.getMessage(); // consumes less memory!
        } catch (UnknownHostException e) {
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }
    }

}
