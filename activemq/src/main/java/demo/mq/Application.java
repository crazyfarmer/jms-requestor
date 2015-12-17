package demo.mq;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import messaging.command.CommandInQueueHandler;
import messaging.command.CommandSender;


/**
 * @author lauwings
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application implements CommandLineRunner {

    private static final int NUMBER_OF_PURCHASE = 10;

	@Autowired
	private PurchaseHandler receiver;
	
	private	CommandInQueueHandler messageInQueueHandler = new CommandInQueueHandler();

	@Autowired
	private PurchaseRequest publisher;

	@Bean
	public CountDownLatch latch() {
		return new CountDownLatch(NUMBER_OF_PURCHASE);
	}
	
	@Bean
	public CommandSender sender() {
		return new CommandSender();
	}


	@Override
	public void run(String... args) throws Exception {
			messageInQueueHandler.setMessageHandler(receiver);
			new Thread(messageInQueueHandler).start();
			publisher.batchPurchase(NUMBER_OF_PURCHASE);
	}

	public static void main(String[] args) throws InterruptedException {
 		ApplicationContext app = SpringApplication.run(Application.class, args);
		
		app.getBean(CountDownLatch.class).await(1, TimeUnit.SECONDS);

 	}

}
