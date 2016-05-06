import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.concurrent.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;
import java.text.DecimalFormat;
import java.awt.Component;

/**
 * TableDemo is a GUI for OANDA prices
 *
 */
public class TableDemo extends JPanel {
    private boolean DEBUG = false;
    MyTableModel tableModel = new MyTableModel(); //not part of original code

    public TableDemo() {
        super(new GridLayout(1,0));

        JTable table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);

        // renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new myDoubleRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new myDoubleRenderer());
    }

    static class myDoubleRenderer extends DefaultTableCellRenderer {
        private static final DecimalFormat formatter = new DecimalFormat( "####.######" );

        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            // First format the cell value as required

            value = formatter.format((Number)value);

            // And pass it on to parent class

            return super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column );
        }
    }



    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {
                "Type",
                "ID",
                "TIME",
                "Bid",
                "Ask",
                "Size"
        };



        private Object[][] data = {

                {"p","EUR/JPY",0,0,0,3000000},
                {"p","EUR/JPY",0,0,0,1000000},

                {"p","USD/CAD",0,0,0,3000000},
                {"p","USD/CAD",0,0,0,1000000},

                {"p","GBP/JPY",0,0,0,3000000},
                {"p","GBP/JPY",0,0,0,1000000},

                {"p","EUR/JPY",0,0,0,3000000},
                {"p","EUR/JPY",0,0,0,1000000},

                {"p","USD/JPY",0,0,0,3000000},
                {"p","USD/JPY",0,0,0,1000000},

                {"p","EUR/GBP",0,0,0,3000000},
                {"p","EUR/GBP",0,0,0,1000000},

                {"p","GBP/USD",0,0,0,3000000},
                {"p","GBP/USD",0,0,0,1000000},

                {"p","EUR/AUD",0,0,0,3000000},
                {"p","EUR/AUD",0,0,0,1000000},

                {"p","USD/SGD",0,0,0,3000000},
                {"p","USD/SGD",0,0,0,1000000},

                {"p","AUD/USD",0,0,0,3000000},
                {"p","AUD/USD",0,0,0,1000000},

                {"p","AUD/JPY",0,0,0,3000000},
                {"p","AUD/JPY",0,0,0,1000000}
        };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */

        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                        + " to " + value
                        + " (an instance of "
                        + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static TableDemo createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TableDemo newContentPane = new TableDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        return newContentPane;
    }

    static TableDemo instance;

    public static void main(String[] args) {

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                instance = createAndShowGUI();
            }
        });

        BlockingQueue queue = new ArrayBlockingQueue(1024);
        Producer producer = new Producer(queue);
//        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
//        new Thread(consumer).start();
        final HashMap hm = new HashMap();

            hm.put(709043012, 0); //EUR/JPY
            hm.put(-57566941, 2); //USD/CAD
            hm.put(1912155393,4); //GBP/JYP
            hm.put(871818727, 6); //EUR/USD
            hm.put(353631269, 8); //USD/JPY
            hm.put(-1324321699,10); //EUR/GBP
            hm.put(1475230038,12); //GBP/USD
            hm.put(1485730906,14); //EUR/AUD
            hm.put(656997788,16); //USD/SGD
            hm.put(-1359928140,18); //AUD/USD
            hm.put(-1373310881,20); //AUD/JPY

        while (true) {
            try {
                Thread.sleep(1);
                ByteBuffer trade    = (ByteBuffer) queue.take();
                final char first    = (char) trade.get();
                final int id        = trade.getInt();
                final long time     = trade.getLong();
                final double bid    = trade.getDouble();
                final double ask    = trade.getDouble();
                final int size      = trade.getInt();

                byte[] array        = new byte[trade.get()];
                trade.get(array);
                String data = new String(array);

                if(size == 1000000 && id == 1475230038) {
                    System.out.println("ask: " + ask);
               }
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {

                        int row = (int) hm.get(id);
                        row = (size == 3000000) ? row : row + 1;

                        long unixSeconds = time;
                        Date date = new Date(unixSeconds);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                        String formattedDate = sdf.format(date);
/*
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // give a timezone reference for formating (see comment at the bottom
*/
                        instance.tableModel.setValueAt(first, row, 0);
                        instance.tableModel.setValueAt(formattedDate, row, 2);
                        // instance.tableModel.setValueAt(time, row, 2);
                        instance.tableModel.setValueAt(bid, row, 3);
                        instance.tableModel.setValueAt(ask, row, 4);
                        instance.tableModel.setValueAt(size, row, 5);

                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

