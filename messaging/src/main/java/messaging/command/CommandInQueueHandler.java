package messaging.command;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * TODO: DOCUMENT ME!
 *
 * @author liusya@yonyou.com
 * @date 11/9/15.
 */
public class CommandInQueueHandler extends ConfigSupport implements MessageListener, Runnable {

	private String queueName = "commands.purchase";

	private String initialContextFactory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
 
	private MessageProducer replyProducer;
	private QueueSession session;

	private CommandHandler messageHandler;
	private QueueConnection queueConnection;
	private Queue queue;

	public CommandInQueueHandler() {
		Properties properties = new Properties();
		properties.put("java.naming.factory.initial", getContextFactory());
		properties.put("java.naming.provider.url", getConnectionString());
		// properties.put("connectionfactory.QueueConnectionFactory",
		// connectionString);
		properties.put("queue." + queueName, queueName);

		try {
			InitialContext ctx = new InitialContext(properties);
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx
					.lookup("QueueConnectionFactory");
			queueConnection = queueConnectionFactory.createQueueConnection();

			session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			queue = (Queue) ctx.lookup(queueName);
			replyProducer = session.createProducer(null);
		} catch (JMSException e) {
			throw new RuntimeException("Error in JMS operations", e);
		} catch (NamingException e) {
			throw new RuntimeException("Error in initial context lookup", e);
		}

	}

	// 注入业务操作
	public void setMessageHandler(CommandHandler handler) {
		this.messageHandler = handler;
	}

	public void run() {
		doReceive();
	}

	public void doReceive() {
		try {
			queueConnection.start();
			MessageConsumer responseConsumer = session.createConsumer(queue);
			responseConsumer.setMessageListener(this);
		} catch (JMSException e) {
			throw new RuntimeException("Error in receiving message", e);
		}
	}

	@Override
	public void onMessage(Message message) {
		Command messageText = null;
		try {
			TextMessage response = this.session.createTextMessage();
			if (message instanceof ObjectMessage) {
				ObjectMessage objectMessage = (ObjectMessage) message;

				messageText = (Command) objectMessage.getObject();
				response.setText(messageHandler.handleCommand(messageText));
			}

			response.setJMSCorrelationID(message.getJMSCorrelationID());
			replyProducer.send(message.getJMSReplyTo(), response);

		} catch (JMSException e) {
			// Handle the exception appropriately
		}
	}
}
