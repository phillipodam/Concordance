package com.stelligent.concordance.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.stelligent.concordance.jms.SubmitFile;

/**
 * @author phillipodam
 *
 * Controller for the file upload
 */
@Controller
public class FileUploadController {
	@Inject
	private SubmitFile producer;
	
	/**
	 * Receive the file upload from the web browser
	 * 
	 * @param file {@link org.springframework.web.multipart.MultipartFile} file being uploaded
	 * @param model {@link org.springframework.ui.Model} 
	 * @return {@link java.lang.String} view to return
	 */
    @RequestMapping(value="/upload", method=RequestMethod.POST, produces={"text/html"})
    public String handleFileUploadHTML(@RequestParam("file") MultipartFile file, Model model) {
        model.addAttribute("filename", file.getOriginalFilename());
    	
        try {
            String id = submitMessage(file);
            model.addAttribute("id", id);
            
            return "uploaded";
        } catch (Exception e) {
        	return "error";
        }
    }
    
    /**
     * Receive the file upload from a REST call
     * 
     * @param file {@link org.springframework.web.multipart.MultipartFile} file being uploaded
     * @return {@link java.util.Map<java.lang.String, java.lang.String>} JSON
     */
    @RequestMapping(value="/upload", method=RequestMethod.POST, produces={"application/json"})
    public @ResponseBody Map<String, String> handleFileUploadJSON(@RequestParam("file") MultipartFile file) {
    	Map<String, String> result = new HashMap<String, String>();
    	
        try {
            result.put("id", submitMessage(file));
            return result;
        } catch (Exception e) {
        	result.put("error", e.getMessage());
        	return result;
        }
    }
    
    private String submitMessage(MultipartFile file) throws IOException {
    	return producer.submitMessage(file.getOriginalFilename(), IOUtils.toString(file.getInputStream()));
    }
}