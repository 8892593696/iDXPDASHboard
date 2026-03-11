package com.pilog.mdm.cloud.Controller;

import com.pilog.mdm.cloud.Service.DashBoardsConversationService;
import jakarta.servlet.http.HttpServletRequest;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashBoardConversationController {
	@Autowired
	public DashBoardsConversationService dashBoardsService;
	
	@RequestMapping(value = "/getConversationalAIMessage", produces = { "application/json" }) 
    public @ResponseBody JSONObject getConversationalAIMessage(HttpServletRequest request) {    
    	JSONObject resultObj = new JSONObject();
    	try {
    		resultObj = dashBoardsService.getConversationalAIMessage(request);           
    	} catch (Exception e) {             
    		e.printStackTrace(); 
    	}
    	
    	return resultObj;
    }
	
	@RequestMapping(value = "/getUserTableNamesData", method = { RequestMethod.GET, RequestMethod.POST })
   	public @ResponseBody JSONObject getUserTableNames(HttpServletRequest request) {    
   		JSONObject resultObject = null;
   		try {
   			resultObject = dashBoardsService.getUserTableNames(request);                           
   		} catch (Exception e) { 
   			e.printStackTrace();
   		}
   		return resultObject;  
   	}
	
	@RequestMapping(value = "/getUserMergeTableNamesData", method = { RequestMethod.GET, RequestMethod.POST })
   	public @ResponseBody JSONObject getUserMergeTableNames(HttpServletRequest request) {    
   		JSONObject resultObject = null;
   		try {
   			resultObject = dashBoardsService.getUserMergeTableNames(request);                            
   		} catch (Exception e) { 
   			e.printStackTrace();
   		}
   		return resultObject;  
   	}
	@RequestMapping(value = "/getUserMergeTableNamesColumns", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody JSONObject getUserMergeTableNamesColumns(HttpServletRequest request) {    
		JSONObject resultObject = null;
		try {
			resultObject = dashBoardsService.getUserMergeTableNamesColumns(request);                           
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return resultObject;  
	}
	
	@RequestMapping(value = "/getUserSearchData", method = { RequestMethod.GET, RequestMethod.POST })
   	public @ResponseBody JSONObject getChatBotResponse(HttpServletRequest request) {
   		JSONObject resultObject = null;
   		try {
   			resultObject = dashBoardsService.getUserSearchData(request);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   		return resultObject;  
   	}
}
