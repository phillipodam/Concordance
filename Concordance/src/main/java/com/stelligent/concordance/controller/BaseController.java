package com.stelligent.concordance.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stelligent.concordance.solution.Concordance;
import com.stelligent.concordance.solution.ConcordanceCache;

/**
 * @author phillipodam
 *
 * Controller for the basic endpoints provided by the application
 */
@Controller
public class BaseController {
	@Inject
	private ConcordanceCache cache;
	
	/**
	 * Return the starting page of the application
	 * 
	 * @return {@link java.lang.String} the name of the view to display
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}
	
	/**
	 * Return the generated concordance
	 * 
	 * @param id {@link java.lang.String} the unique identifier for the concordance
	 * @return the concordance in JSON format
	 */
	@RequestMapping(value="/concordance/{id:.*}", method=RequestMethod.GET, produces={"application/json"})
	public @ResponseBody Concordance concordance(@PathVariable("id") String id) {
		return this.cache.getConcordance(id);
	}
}