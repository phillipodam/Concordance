package com.stelligent.concordance.solution.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stelligent.concordance.solution.Concordance;
import com.stelligent.concordance.solution.ConcordanceBuilder;
import com.stelligent.concordance.solution.ConcordanceInputValidator;

/**
 * @author phillipodam
 *
 * Parse document and generate concordance
 */
@Component
public class ConcordanceBuilderImpl implements ConcordanceBuilder {
	private static final Logger LOG = Logger.getLogger(ConcordanceBuilderImpl.class);
	
	@Value(value="${sentence.model}")
	private String sentenceModel;
	@Value(value="${word.model}")
	private String wordModel;
	
	@Value("${sentence.detector.internal.error}")
	private String sentenceDetectorError;
	@Value("${word.tokenizer.internal.error}")
	private String wordTokenizerError;
	@Value("${content.requirements}")
	private String contentRequirements;
	
	/**
	 * Generate concordance using Apache openNLP
	 */
	public Concordance build(String content) {
		Concordance concordance = new ConcordanceImpl();
		
		if (ConcordanceInputValidator.isValid(content)) {
			SentenceDetector sentenceDetector = createSentenceDetector();
			Tokenizer wordTokenizer = createWordTokenizer();
			
			if (sentenceDetector == null) {
				concordance.setError(this.sentenceDetectorError);
			} else if (wordTokenizer == null) {
				concordance.setError(this.wordTokenizerError);
			} else {
				String[] sentences = sentenceDetector.sentDetect(content);
				
				for (int i = 0; i < sentences.length; i++) {
					String[] words = wordTokenizer.tokenize(sentences[i]);
					
					for (int j = 0; j < words.length; j++) {
						if (!Pattern.matches("\\p{Punct}", words[j])) {
							concordance.addToConcordance(words[j], i + 1);
						}
					}
				}
			}
		} else {
			concordance.setError(this.contentRequirements);
		}
		
		return concordance;
	}
	
	private SentenceDetector createSentenceDetector() {
		InputStream modelIn = null;
		SentenceDetector detector = null;
		
		try {
			modelIn = new FileInputStream(getClass().getClassLoader().getResource(this.sentenceModel).getFile());
			SentenceModel model = new SentenceModel(modelIn);
			detector = new SentenceDetectorME(model);
		} catch (IOException e) {
			LOG.debug("Exception occurred while creating sentence detector", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) { }
			}
		}
		
		return detector;
	}
	
	private Tokenizer createWordTokenizer() {
		InputStream modelIn = null;
		Tokenizer tokenizer = null;
		
		try {
			modelIn = new FileInputStream(getClass().getClassLoader().getResource(this.wordModel).getFile());
			TokenizerModel model = new TokenizerModel(modelIn);
			tokenizer = new TokenizerME(model);
		} catch (IOException e) {
			LOG.debug("Exception occurred while creating word tokenizer", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) { }
			}
		}
		
		return tokenizer;
	}
}