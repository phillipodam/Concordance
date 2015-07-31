package com.stelligent.concordance.jms;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.stelligent.concordance.solution.ConcordanceCache;

/**
 * @author phillipodam
 *
 * JMS consumer for getting file from queue
 */
@Component
public class ConsumeFile implements MessageListener {
	private static final Logger LOG = Logger.getLogger(ConsumeFile.class);
	
	@Inject
	private ConcordanceCache cache;
	
	/**
	 * Consume message from queue
	 * 
	 * @param message {@link javax.jms.Message} containing file placed on queue
	 */
	@Override
	public void onMessage(Message message) {
		try {
			String id = message.getStringProperty(SubmitFile.ID);
			String filename = message.getStringProperty(SubmitFile.FILENAME);
			String content = message.getStringProperty(SubmitFile.CONTENT);
			
			if (LOG.isTraceEnabled()) LOG.trace(String.format("Consuming message for file %s with id %s", filename, id));
			
			this.cache.addToCache(id, filename, content);
		} catch (JMSException e) {
			LOG.debug("Exception occurred attempting to consume message", e);
		}
	}
}