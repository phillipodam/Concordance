package com.stelligent.concordance.solution;

import com.stelligent.concordance.solution.Concordance;

/**
 * @author phillipodam
 *
 * Interface for building a concordance
 */
public interface ConcordanceBuilder {
	/**
	 * Generate concordance from provided content
	 * 
	 * @param content {@link java.lang.String} used to construct the concordance
	 * @return {@link com.stelligent.concordance.solution.Concordance} constructed concordance
	 */
	public Concordance build(String content);
}