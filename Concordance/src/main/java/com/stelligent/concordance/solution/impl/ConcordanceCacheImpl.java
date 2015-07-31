package com.stelligent.concordance.solution.impl;

import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stelligent.concordance.solution.Concordance;
import com.stelligent.concordance.solution.ConcordanceBuilder;
import com.stelligent.concordance.solution.ConcordanceCache;

/**
 * @author phillipodam
 *
 * Concordance cache
 */
@Component
public class ConcordanceCacheImpl implements ConcordanceCache {
	private Map<String, Concordance> cache;
	
	@Inject
	private ConcordanceBuilder builder;
	
	@Value("${cache.ttl}")
	private long timeout;
	
	@PostConstruct
	private void postConstruct() {
		this.cache = Collections.synchronizedMap(new PassiveExpiringMap<String, Concordance>(this.timeout));
	}
	
	/**
	 * Given content generate concordance and add it to the cache
	 */
	public void addToCache(String id, String filename, String content) {
		this.cache.put(id, this.builder.build(content));
	}
	
	/**
	 * Get concordance from cache via the unique identifier
	 */
	public Concordance getConcordance(String id) {
		return this.cache.get(id);
	}
}