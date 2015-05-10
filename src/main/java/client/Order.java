package client;

import market_proto.Market;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by max on 9/5/15.
 */

public class Order {

    private static Logger logger = Logger.getLogger(Order.class);
    public enum ORDER_STATUS {
        ACK, NACKED, REJECTED, FILLED, CANCELLED
    }

    private Integer id;
    private String instrument;
    private Long quantity;
    private Float price;
    private Market.Order.OrderWay way;

    public ORDER_STATUS getStatus() {
        return status;
    }

    public void setStatus(ORDER_STATUS status) {
        this.status = status;
    }

    private ORDER_STATUS status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Market.Order.OrderWay getWay() {
        return way;
    }

    public void setWay(Market.Order.OrderWay way) {
        this.way = way;
    }

    public void writeNewToStream(OutputStream out) throws IOException {

        Market.Order order = Market.Order.newBuilder().setInstrument(getInstrument()).setPrice(getPrice()).
                setOrderType(Market.Order.OrderType.NEW).setWay(getWay()).setQuantity(getQuantity()).setOrderId(getId()).build();
        order.writeDelimitedTo(out);
    }

    // Parse strings such as "B 12 GoogleInc at 99.2"
    public static Order fromString(String text)  {
        Order order = new Order();

        Pattern orderPattern = Pattern.compile("(?<way>[BS]) (?<qty>\\d+) (?<instr>\\w+) at (?<price>\\d+(\\.\\d+)?)");
        Matcher m = orderPattern.matcher(text);

        if (m.find()){
            if (m.group("way").equals("B")) {
                order.setWay(Market.Order.OrderWay.BUY);
            } else {
                order.setWay(Market.Order.OrderWay.SELL);
            }

            order.setQuantity(Long.parseLong(m.group("qty")));
            order.setInstrument(m.group("instr"));
            order.setPrice(Float.parseFloat(m.group("price")));
        } else {
            throw new IllegalArgumentException();
        }

        return order;
    }

    public void updateOrder(Market.UpdateOrder update) {
        if (update.getStatus() == Market.UpdateOrder.OrderStatus.ACK) {
            this.setStatus(ORDER_STATUS.ACK);
            logger.warn(String.format("Order %d acked.", getId()));
        } else if (update.getStatus() == Market.UpdateOrder.OrderStatus.NACK) {
            this.setStatus(ORDER_STATUS.REJECTED);
            logger.warn(String.format("Order %d rejected for the following reason: %s", getId(), update.getMessage()));
            return;
        }

        if ( update.hasExecQty() && update.getExecQty() > 0) {

        }


    }


}

