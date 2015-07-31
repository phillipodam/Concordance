package com.stelligent.concordance.solution;

import java.util.List;

/**
 * @author phillipodam
 *
 * Interface providing access to the properties of an individual entry in the concordance
 */
public interface OccurrenceAndAppearance {
	/**
	 * Increment the number of occurrences
	 * 
	 * @return self
	 */
	public OccurrenceAndAppearance incrementOccurrence();
	
	/**
	 * Get the number of occurrences
	 * 
	 * @return number of occurrences
	 */
	public long getOccurrence();
	
	/**
	 * Add an appearance
	 * 
	 * @param appearance location of occurrence
	 * @return self
	 */
	public OccurrenceAndAppearance addAppearance(long appearance) throws IllegalArgumentException;
	
	/**
	 * Get the list of appearances
	 * 
	 * @return {@link java.util.List} of locations occurrence appeared
	 */
	public List<Long> getAppearances();
}