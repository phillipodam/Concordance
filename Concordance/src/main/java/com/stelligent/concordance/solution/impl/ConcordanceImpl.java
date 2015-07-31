package com.stelligent.concordance.solution.impl;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

import com.stelligent.concordance.solution.Concordance;
import com.stelligent.concordance.solution.OccurrenceAndAppearance;

/**
 * @author phillipodam
 *
 * Store an individual concordance
 */
public class ConcordanceImpl implements Concordance {
	private Map<String, OccurrenceAndAppearance> concordance = new TreeMap<String, OccurrenceAndAppearance>();
	private String creationError = null;
	
	/**
	 * Add a word occurrence to concordance
	 */
	public void addToConcordance(String word, long appearance) {
		this.creationError = null;
		word = word.toLowerCase();
		
		OccurrenceAndAppearance occurrenceAndAppearance;
		
		if (this.concordance.containsKey(word)) {
			occurrenceAndAppearance = this.concordance.get(word);
		} else {
			occurrenceAndAppearance = new OccurrenceAndAppearanceImpl();
			this.concordance.put(word, occurrenceAndAppearance);
		}
		
		occurrenceAndAppearance.incrementOccurrence().addAppearance(appearance);
	}
	
	public Map<String, OccurrenceAndAppearance> getConcordance() {
		return this.concordance;
	}
	
	public void setError(String creationError) {
		this.concordance.clear();
		this.creationError = creationError;
	}
	
	public String getError() {
		return this.creationError;
	}
	
	/**
	 * @author phillipodam
	 *
	 * Providing access to the properties of an individual entry in the concordance
	 */
	public class OccurrenceAndAppearanceImpl implements OccurrenceAndAppearance {
		private long occurrence = 0;
		private List<Long> appearances = new ArrayList<Long>();
		
		public OccurrenceAndAppearance incrementOccurrence() {
			this.occurrence += 1;
			return this;
		}
		
		public long getOccurrence() {
			return this.occurrence;
		}
		
		public OccurrenceAndAppearance addAppearance(long appearance) throws IllegalArgumentException {
			if (appearance < 1) throw new IllegalArgumentException("appearance must be 1 or greater");
			this.appearances.add(appearance);
			return this;
		}
		
		public List<Long> getAppearances() {
			return this.appearances;
		}
	}
}