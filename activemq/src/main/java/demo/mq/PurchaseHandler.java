package demo.mq;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import messaging.command.Command;
import messaging.command.CommandHandler;

/**
 * TODO: DOCUMENT ME!
 *
 * @author liusya@yonyou.com
 * @date 11/11/15.
 */
@Service public class PurchaseHandler implements CommandHandler {

    @Autowired CountDownLatch latch;

    @Override public String handleCommand(Command command) {
        System.out.println("调用建行支付接口");
		System.out.println("Purchased Money : " + ((Purchase) command).getMoney());
        latch.countDown();
        return "I have purchased";

    }
}
