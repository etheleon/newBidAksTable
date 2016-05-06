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

/**
 * Created by uesu on 13/4/16.
 */

public class Consumer implements Runnable {

    protected BlockingQueue queue = null;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while(true) {
                //GUI App
//                bidAskTable app = new bidAskTable();
//                app.setVisible(true);
                //ByteBuffer
                ByteBuffer trade = (ByteBuffer) queue.take();
                char first = (char) trade.get();
                int id = trade.getInt();
                long time = trade.getLong();
                double bid = trade.getDouble();
                double ask = trade.getDouble();
                int size = trade.getInt();
                byte[] array = new byte[trade.get()];
                trade.get(array);
                String data = new String(array);

                    System.out.println("First: " + first);
                    System.out.println("ID: " + id);
                    System.out.println("Time: " + time);
                    System.out.println("Bid: " + bid);
                    System.out.println("Ask: " + ask);
                    System.out.println("Size: " + size);
                    System.out.println("Data: " + data);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
