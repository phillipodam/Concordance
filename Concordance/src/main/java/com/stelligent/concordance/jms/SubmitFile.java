package com.stelligent.concordance.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * @author phillipodam
 *
 * JMS producer for adding file to queue
 */
@Component
public class SubmitFile {
	public static final String ID = "id";
	public static final String FILENAME = "filename";
	public static final String CONTENT = "content";
	
	private static final Logger LOG = Logger.getLogger(SubmitFile.class);
	
	@Inject
	private JmsTemplate template;
	
	/**
	 * Add file to message queue
	 * 
	 * @param filename {@link java.lang.String} name of file being put on queue (for debugging purposes)
	 * @param content {@link java.lang.String} contents of file being put on queue
	 * @return {@link java.lang.String} unique identifier associated with file
	 */
	public String submitMessage(final String filename, final String content) {
		final String id = java.util.UUID.randomUUID().toString();
		
		try {
			template.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					Message message = session.createMessage();
					message.setStringProperty(ID, id);
					message.setStringProperty(FILENAME, filename);
					message.setStringProperty(CONTENT, content);
					
					if (LOG.isTraceEnabled()) LOG.trace(String.format("Sending message for file %s with id %s", filename, id));
					
					return message;
				}
			});
		} catch (Exception e) {
			LOG.debug(String.format("Exception occurred while producing message for file: %s", filename), e);
		}
		
		return id;
	}
}