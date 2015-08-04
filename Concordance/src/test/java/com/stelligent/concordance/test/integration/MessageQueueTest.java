package com.stelligent.concordance.test.integration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import com.stelligent.concordance.jms.SubmitFile;
import com.stelligent.concordance.solution.ConcordanceCache;
import com.stelligent.concordance.test.util.Rest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class MessageQueueTest {
	@Inject
	private SubmitFile producer;
	
	@Inject
	private ConcordanceCache cache;
	
	/**
	 * Attempt generating concordance of a valid file
	 * 
	 * Expected result: Successfully generated concordance
	 */
	@Test
	public void acceptableProcessingTimeViaJMS() {
		/*
		 * Considering the simplicity of this application, the isolated testing of the queue
		 * independent of the controller does not add much value. In a larger application
		 * however it would help in isolating where exactly an issue resides.
		 */
		
		String filepath = "com/stelligent/concordance/documents/plain text english document.txt";
	    InputStream is = getClass().getClassLoader().getResourceAsStream(filepath);
		
		try {
			String id = this.producer.submitMessage(new File(filepath).getName(), IOUtils.toString(is));
			
			// length of time considered acceptable for message to be consumed off queue, processed and added to cache, 1 second is very generous
			Rest.sleep(1000);
			
			Assert.notNull(this.cache.getConcordance(id));
		} catch (IOException e) {
			Assert.isTrue(false);
		}
	}
}