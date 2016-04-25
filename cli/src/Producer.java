import java.io.IOException;
import java.net.*;
import java.nio.*;
import java.awt.*;
import java.text.ParseException;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*; //required for TableCellRenderer
import java.util.concurrent.*;
import java.lang.Thread;

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
