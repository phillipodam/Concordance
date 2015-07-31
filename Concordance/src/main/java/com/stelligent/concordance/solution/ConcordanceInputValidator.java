package com.stelligent.concordance.solution;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author phillipodam
 *
 * Concordance data validator
 */
public class ConcordanceInputValidator {
	/**
	 * Verify data for concordance is valid
	 * 
	 * @param content {@link java.lang.String} to check before generating concordance
	 * @return true if provided string contains 1 or more characters that are only punctuation, alphanumeric and whitespace
	 */
	public static boolean isValid(String content) {
		return StringUtils.isNotEmpty(content) && Pattern.matches("(\\p{Punct}|\\p{Alnum}|\\p{Space})++", content);
	}
}