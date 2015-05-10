package input;

import client.OrderBook;
import market_proto.Market;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by max on 9/5/15.
 */
public class MarketListener implements Runnable {

    private Socket socket;
    private OrderBook orderBook;
    private Logger logger = Logger.getLogger(getClass());


    public MarketListener(Socket socket, OrderBook orderBook) {
        this.socket = socket;
        this.orderBook = orderBook;
    }

    @Override
    public void run() {
        Market.UpdateOrder connectionResponse = null;
        try {
            Market.UpdateOrder update = Market.UpdateOrder.parseDelimitedFrom(socket.getInputStream());
            logger.info("Connection received update order from  from exchange: " + update.toString().replaceAll("\n", " ; "));
            orderBook.updateBook(update);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
