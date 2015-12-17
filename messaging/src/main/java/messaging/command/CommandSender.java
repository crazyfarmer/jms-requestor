package messaging.command;

import java.util.Properties;
import java.util.Random;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CommandSender extends ConfigSupport implements MessageListener {

	private String queueName = "commands.purchase";

	private String initialContextFactory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
	private String connectionString = "tcp://localhost:61616";

	private MessageProducer messageProducer;
	private Session session;

	public CommandSender() {
		Properties properties = new Properties();
		QueueConnection queueConnection = null;
		properties.put("java.naming.factory.initial", getContextFactory());
		properties.put("java.naming.provider.url", getConnectionString());
		// properties.put("connectionfactory.QueueConnectionFactory",
		// connectionString);
		properties.put("queue." + queueName, queueName);

		try {
			// initialize
			// the required connection factories
			InitialContext ctx = new InitialContext(properties);
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx
					.lookup("QueueConnectionFactory");
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueConnection.start();
			session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			Queue queue = (Queue) ctx.lookup(queueName);
			messageProducer = session.createProducer(queue);
		} catch (JMSException e) {
			throw new RuntimeException("Error in JMS operations", e);
		} catch (NamingException e) {
			throw new RuntimeException("Error in initial context lookup", e);
		}
	}

	public void doSend(Command command) {
		try {
			ObjectMessage textMessage = session.createObjectMessage(command);
			String correlationId = this.createRandomID();
			textMessage.setJMSCorrelationID(correlationId);

			Destination tempDest = session.createTemporaryQueue();
			MessageConsumer responseConsumer = session.createConsumer(tempDest);
			textMessage.setJMSReplyTo(tempDest);
			responseConsumer.setMessageListener(this);

			messageProducer.send(textMessage);
		} catch (JMSException e) {
			throw new RuntimeException("Error in JMS operations", e);
		}
	}

	@Override
	public void onMessage(Message message) {
		String messageText = null;
		try {
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				messageText = textMessage.getText();
				System.out.println("Receive: " + messageText);
			}
		} catch (JMSException e) {
			// Handle the exception appropriately
		}
	}

	private String createRandomID() {
		Random random = new Random(System.currentTimeMillis());
		long randomLong = random.nextLong();
		return Long.toHexString(randomLong);
	}
}
