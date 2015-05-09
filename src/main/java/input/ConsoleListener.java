package input;

import client.Order;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by max on 9/5/15.
 */
public class ConsoleListener implements  Runnable{

    private Logger logger = Logger.getLogger(getClass());

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
