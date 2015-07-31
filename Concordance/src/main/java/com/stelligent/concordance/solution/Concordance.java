package com.stelligent.concordance.solution;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author phillipodam
 *
 * Interface providing ways in which you can interact with the concordance
 */
@JsonSerialize(using=ConcordanceSerializer.class)
public interface Concordance {
	/**
	 * Add to concordance
	 * 
	 * @param word {@link java.lang.String} being added to concordance
	 * @param appearance location occurrence appeared
	 */
	public void addToConcordance(String word, long appearance);
	
	/**
	 * Get constructed concordance
	 * 
	 * @return {@link java.util.Map} of constructed concordance
	 */
	public Map<String, OccurrenceAndAppearance> getConcordance();
	
	/**
	 * Set error raised while generating concordance
	 * 
	 * @param creationError {@link java.lang.String} description of error
	 */
	public void setError(String creationError);
	
	/**
	 * Get error raised while generating concordance
	 * 
	 * @return {@link java.lang.String} description of error
	 */
	public String getError();
}