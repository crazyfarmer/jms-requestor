package reactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@Service public class PurchaseRequest {

    @Autowired EventBus eventBus;

    @Autowired CountDownLatch latch;

    public void batchPurchase(int numberOfPurchase) throws InterruptedException {
        long start = System.currentTimeMillis();

        for (int i = 0; i < numberOfPurchase; i++) {
            eventBus.notify("purchase", Event.wrap(new Purchase(genRandomMoney())));
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
