package messaging.config;

import lombok.Getter;

public class MessagingConfig {
	public final String REMOTE_KEY = "messaging.mq.remotehost"; 
	public final String REMOTE_PORT = "messaging.mq.remoteport"; 
	
 	
 	public String getRemoteHost(){
 		return System.getProperty(REMOTE_KEY)==null ?"localhost":System.getProperty(REMOTE_KEY);
 	}
	
	 
	public String getRemotePort(){
		return System.getProperty(REMOTE_PORT)==null ?"61616":System.getProperty(REMOTE_PORT);
	}
	
}
