package input;

import market_proto.Market;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by max on 9/5/15.
 */
public class MarketListener implements Runnable {

    private Socket socket;
    private Logger logger = Logger.getLogger(getClass());

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public MarketListener(Socket socket) {
        setSocket(socket);
    }

    @Override
    public void run() {
        Market.UpdateOrder connectionResponse = null;
        try {
            Market.UpdateOrder update = Market.UpdateOrder.parseFrom(socket.getInputStream());
            logger.info("Connection received update order from  from exchange: " + update.toString().replaceAll("\n", " ; "));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
