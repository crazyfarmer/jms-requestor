package messaging.command;

import messaging.config.MessagingConfig;

public abstract class ConfigSupport {

	private static final String TCP = "tcp://";

	private final MessagingConfig config = new MessagingConfig();
	
	private String initialContextFactory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
	
	private String connectionString = "tcp://localhost:61616";
	
	protected String getConnectionString(){
		StringBuilder  sb = new StringBuilder();
		sb.append(TCP).append(config.getRemoteHost()).append(":").append(config.getRemotePort());
		return sb.toString();
	}
	
	protected String getContextFactory(){
		return initialContextFactory;
	}
}
