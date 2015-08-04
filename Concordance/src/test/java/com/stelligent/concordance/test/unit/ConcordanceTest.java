package com.stelligent.concordance.test.unit;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import com.stelligent.concordance.solution.Concordance;
import com.stelligent.concordance.solution.ConcordanceCache;
import com.stelligent.concordance.solution.OccurrenceAndAppearance;
import com.stelligent.concordance.solution.impl.ConcordanceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
public class ConcordanceTest {
	private static final Logger LOG = Logger.getLogger(ConcordanceTest.class);
	
	@Inject
	private ConcordanceCache cache;
	
	@Value("${content.requirements}")
	private String contentRequirements;
	
	/**
	 * Attempt generating concordance of null
	 * 
	 * Expected result: Error message returned
	 */
	@Test
	public void testNull() {
		this.cache.addToCache("null", "", null);
		Assert.isTrue(this.cache.getConcordance("null").getError().equals(this.contentRequirements));
	}
	
	/**
	 * Attempt generating concordance of empty string
	 * 
	 * Expected result: Error message returned
	 */
	@Test
	public void testEmpty() {
		this.cache.addToCache("blank", "", "");
		Assert.isTrue(this.cache.getConcordance("blank").getError().equals(this.contentRequirements));
	}
	
	/**
	 * Attempt generating concordance of valid string
	 * 
	 * Expected result: Successfully generated concordance
	 */
	@Test
	public void testSetText() {
		this.cache.addToCache("text", "", "These cases are perfectly simple and easy to distinguish.");
		Map<String, OccurrenceAndAppearance> concordance = this.cache.getConcordance("text").getConcordance();
		
		//printConcordance(this.cache.getConcordance("text"));
		
		Assert.isTrue(concordance.size() == 9, "Incorrect number of words in concordance");
		
		for (Map.Entry<String, OccurrenceAndAppearance> entry: concordance.entrySet()) {
			Assert.isTrue(entry.getValue().getOccurrence() == 1, "Incorrect number of occurrences");
			Assert.isTrue(entry.getValue().getAppearances().size() == 1, "Incorrect number of appearances");
			Assert.isTrue(entry.getValue().getAppearances().get(0).longValue() == 1, "Incorrect appearance location");
		}
	}
	
	/**
	 * Attempt generating concordance of valid single line document
	 * 
	 * Expected result: Successfully generated concordance
	 */
	@Test
	public void testSingleLineDocument() {
		String filepath = "com/stelligent/concordance/documents/plain text english document.txt";
		File file = new File(filepath);
		
		try {
			this.cache.addToCache("single line document", file.getName(), IOUtils.toString(getClass().getClassLoader().getResourceAsStream(filepath)));
			
			//printConcordance(this.cache.getConcordance("single line document"));
			
			// simply confirm concordance generated is not null
			Assert.notNull(this.cache.getConcordance("single line document"));
		} catch (IOException e) {
			Assert.isTrue(false, e.getMessage());
		}
	}
	
	/**
	 * Attempt generating concordance of valid multiple line document
	 * 
	 * Expected result: Successfully generated concordance
	 */
	@Test
	public void testMultiLineDocument() {
		String filepath = "com/stelligent/concordance/documents/multiline plain text english document.txt";
		File file = new File(filepath);
		
		try {
			this.cache.addToCache("multi line document", file.getName(), IOUtils.toString(getClass().getClassLoader().getResourceAsStream(filepath)));
			
			//printConcordance(this.cache.getConcordance("multi line document"));
			
			// simply confirm concordance generated is not null
			Assert.notNull(this.cache.getConcordance("multi line document"));
		} catch (IOException e) {
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
		String filepath = "com/stelligent/concordance/documents/bad file.jpg";
		File file = new File(filepath);
		
		try {
			this.cache.addToCache("bad file", file.getName(), IOUtils.toString(getClass().getClassLoader().getResourceAsStream(filepath)));
			
			//printConcordance(this.cache.getConcordance("bad file"));
			
			// simply confirm concordance generated is not null
			Assert.isTrue(this.cache.getConcordance("bad file").getError().equals(this.contentRequirements));
		} catch (IOException e) {
			Assert.isTrue(false, e.getMessage());
		}
	}
	
	/**
	 * Ensure concordance properties meets expectations
	 * 
	 * Expected result: Generated concordance meets expectations
	 */
	@Test
	public void validTestConcordance() {
		Concordance concordance = new ConcordanceImpl();
		
		concordance.addToConcordance("a", 1);
		concordance.addToConcordance("a", 4);
		concordance.addToConcordance("a", 7);
		
		//printConcordance(concordance);
		
		Assert.isTrue(concordance.getConcordance().size() == 1, "Incorrect number of words in concordance");
		Assert.isTrue(concordance.getConcordance().get("a").getOccurrence() == 3, "Incorrect number of occurrences");
		Assert.isTrue(concordance.getConcordance().get("a").getAppearances().size() == 3, "Incorrect number of appearances");
		Assert.isTrue(concordance.getConcordance().get("a").getAppearances().get(0).longValue() == 1, "Incorrect appearance location for first occurrence");
		Assert.isTrue(concordance.getConcordance().get("a").getAppearances().get(1).longValue() == 4, "Incorrect appearance location for second occurrence");
		Assert.isTrue(concordance.getConcordance().get("a").getAppearances().get(2).longValue() == 7, "Incorrect appearance location for third occurrence");
	}
	
	/**
	 * Attempt to set appearance position of word in concordance to 0
	 * 
	 * Expected result: Exception thrown
	 */
	@Test
	public void invalidTestConcordance() {
		Concordance concordance = new ConcordanceImpl();
		
		try {
			concordance.addToConcordance("a", 0);
			Assert.isTrue(false, "Incorrectly accepted appearance location less than 1");
		} catch (Exception e) {
			Assert.isTrue(true);
		}
	}
	
	@SuppressWarnings(value = { "unused" })
	private void printConcordance(Concordance concordance) {
		int i = 0;
		
		for (Map.Entry<String, OccurrenceAndAppearance> entry : concordance.getConcordance().entrySet()) {
			LOG.info(String.format("%d. %s {%d:%s}", ++i, entry.getKey(), entry.getValue().getOccurrence(), StringUtils.join(entry.getValue().getAppearances(), ",")));
		}
	}
}