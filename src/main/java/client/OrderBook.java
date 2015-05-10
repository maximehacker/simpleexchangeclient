package client;

import market_proto.Market;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Created by max on 9/5/15.
 */
public class OrderBook {

    private static Logger logger = Logger.getLogger(OrderBook.class);
    private Map<Integer, Order> orderMap = new HashMap<>();
    private static Semaphore mutex = new Semaphore(1);;
    private static Integer nextOrderId = 1;


    public Integer newOrder(Order newOrder) {
        synchronized (mutex) {
            logger.info("setting client order id to " + this.nextOrderId.toString());
            newOrder.setId(this.nextOrderId++);
            orderMap.put(newOrder.getId(), newOrder);
        }
        return newOrder.getId();
    }

    public void updateBook(Market.UpdateOrder update) {

        if ( ! this.orderMap.containsKey(update.getOrderId())) {
            logger.warn("Received update for unknown order id : " + update.getOrderId());
            return;
        }

        Order matchingOrder = orderMap.get(update.getOrderId());
        matchingOrder.updateOrder(update);


    }
}
