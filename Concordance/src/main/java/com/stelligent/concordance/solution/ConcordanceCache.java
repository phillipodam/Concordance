package com.stelligent.concordance.solution;

/**
 * @author phillipodam
 *
 * Interface to cache of concordances
 */
public interface ConcordanceCache {
	/**
	 * Add a new concordance
	 * 
	 * @param id {@link java.lang.String} unique identifier for concordance
	 * @param filename {@link java.lang.String} name of file content is sourced from (for debugging purposes)
	 * @param content {@link java.lang.String} used to generate concordance
	 */
	public void addToCache(String id, String filename, String content);

	/**
	 * Provide access to concordance via unique identifier
	 * 
	 * @param id {@link java.lang.String} unique identifier
	 * @return {@link com.stelligent.concordance.solution.Concordance} stored concordance
	 */
	public Concordance getConcordance(String id);
}