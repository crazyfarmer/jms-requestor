package mq;

import command.CommandSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * TODO: DOCUMENT ME!
 *
 * @author liusya@yonyou.com
 * @date 11/11/15.
 */
@Service public class PurchaseRequest {


    @Autowired CountDownLatch latch;

    private CommandSender commandSender = new CommandSender();

    public void batchPurchase(int numberOfPurchase) throws InterruptedException {
        long start = System.currentTimeMillis();


        for (int i = 0; i < numberOfPurchase; i++) {
            commandSender.doSend(new Purchase(genRandomMoney()));
        }

        latch.await();

        long elapsed = System.currentTimeMillis() - start;

        System.out.println("花费时间: " + elapsed + "ms");
        System.out.println("平均每笔支付时间: " + elapsed / numberOfPurchase + "ms");
    }
    public int genRandomMoney() {
        Random r = new Random();
        return (r.nextInt(100) + 1)*100;
    }
}
