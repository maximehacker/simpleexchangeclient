package input;

import client.Order;
import client.OrderBook;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by max on 9/5/15.
 */
public class ConsoleListener implements  Runnable{

    private Logger logger = Logger.getLogger(getClass());
    private Socket socket;
    private OrderBook orderBook;


    public ConsoleListener(Socket socket, OrderBook orderBook) {
        this.socket =  socket;
        this.orderBook = orderBook;


    }

    @Override
    public void run() {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        while (!exit) {
            logger.info("reading from commandline");
            String command = null;
            try {
                command = bufferedReader.readLine();
                if (command.trim().equals("quit")) {
                    exit = true;
                } else {
                    try {
                        Order order = Order.fromString(command);
                        orderBook.newOrder(order);
                        synchronized (socket) {
                            order.writeNewToStream(socket.getOutputStream());
                            socket.getOutputStream().flush();
                        }

                        logger.info(order.toString());
                    } catch (IllegalArgumentException e) {
                        logger.warn("Could not parse order from text: " + command);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                exit =  true;
            }

        }
    }
}
