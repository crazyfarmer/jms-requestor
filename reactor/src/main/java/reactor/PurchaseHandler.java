package reactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.concurrent.CountDownLatch;

@Service class PurchaseHandler implements Consumer<Event<Purchase>> {

    @Autowired CountDownLatch latch;

    public void accept(Event<Purchase> ev) {
        System.out.println("调用建行支付接口");
        System.out.println("Purchased Money : " + ((Purchase) ev.getData()).getMoney());
        latch.countDown();
    }

}
