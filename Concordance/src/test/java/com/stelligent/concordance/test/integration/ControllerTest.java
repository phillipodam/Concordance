package com.stelligent.concordance.test.integration;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import com.stelligent.concordance.controller.BaseController;
import com.stelligent.concordance.controller.FileUploadController;
import com.stelligent.concordance.solution.Concordance;
import com.stelligent.concordance.test.util.Rest;

/*
 * Ideally integration tests would include use of an embedded application server to verify
 * that at least the endpoints are configured correctly and returning the expected results.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class ControllerTest {
	@Inject
	private FileUploadController upload;
	
	@Inject
	private BaseController base;
	
	@Value("${content.requirements}")
	private String contentRequirements;
	
	/**
	 * Attempt generating concordance of a valid file
	 * 
	 * Expected result: Successfully generated concordance
	 */
	@Test
	public void testSingleLineDocument() {
	    try {
	    	String filepath = "com/stelligent/concordance/documents/plain text english document.txt";
	    	InputStream stream = getClass().getClassLoader().getResourceAsStream(filepath);
	    	
	    	Map<String, String> json = upload.handleFileUploadJSON(new MockMultipartFile(new File(filepath).getName(), stream));
	    	
	    	// length of time considered acceptable for message to be consumed off queue, processed and added to cache, 1 second is very generous
	    	Rest.sleep(1000);
	    	
	    	Concordance concordance = base.concordance(json.get("id"));
	    	
	    	Assert.isTrue(concordance.getError() == null && concordance.getConcordance().size() > 0);
	    } catch (Exception e) {
	    	Assert.isTrue(false, e.getMessage());
	    }
	}
	
	/**
	 * Attempt generating concordance of an emty file
	 * 
	 * Expected result: Error message returned
	 */
	@Test
	public void testEmpty() {
	    try {
	    	String filepath = "com/stelligent/concordance/documents/empty file.txt";
	    	InputStream stream = getClass().getClassLoader().getResourceAsStream(filepath);
	    	
	    	Map<String, String> json = upload.handleFileUploadJSON(new MockMultipartFile(new File(filepath).getName(), stream));
	    	
	    	// length of time considered acceptable for message to be consumed off queue, processed and added to cache, 1 second is very generous
	    	Rest.sleep(1000);
	    	
	    	Concordance concordance = base.concordance(json.get("id"));
	    	
	    	Assert.isTrue(concordance.getError().equals(this.contentRequirements) && concordance.getConcordance().size() == 0);
	    } catch (Exception e) {
	    	Assert.isTrue(false, e.getMessage());
	    }
	}
	
	/**
	 * Attempt generating concordance of invalid file
	 * 
	 * Expected result: Error message returned
	 */
	@Test
	public void testBadFile() {
	    try {
	    	String filepath = "com/stelligent/concordance/documents/bad file.jpg";
	    	InputStream stream = getClass().getClassLoader().getResourceAsStream(filepath);
	    	
	    	Map<String, String> json = upload.handleFileUploadJSON(new MockMultipartFile(new File(filepath).getName(), stream));
	    	
	    	// length of time considered acceptable for message to be consumed off queue, processed and added to cache, 1 second is very generous
	    	Rest.sleep(1000);
	    	
	    	Concordance concordance = base.concordance(json.get("id"));
	    	
	    	Assert.isTrue(concordance.getError().equals(this.contentRequirements) && concordance.getConcordance().size() == 0);
	    } catch (Exception e) {
	    	Assert.isTrue(false, e.getMessage());
	    }
	}
}