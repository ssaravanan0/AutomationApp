package com.waitrose.app.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.waitrose.app.dao.ScriptInputsRepository;
import com.waitrose.app.dao.ScriptMasterDao;
import com.waitrose.app.dao.ScriptMasterRepository;
import com.waitrose.app.entity.ScriptInputs;
import com.waitrose.app.entity.ScriptMaster;
import com.waitrose.app.form.Response;

@RestController
@RequestMapping("/api")
public class RestWebController {
	
	private static final Logger logger = LoggerFactory.getLogger(RestWebController.class);

	@Autowired
	private ScriptMasterDao scriptMasterDao;
	
	@Autowired
	private ScriptMasterRepository scriptMasterRepository;
	
	@Autowired
	private ScriptInputsRepository scriptInputsRepository;

	//List<Params> cust = new ArrayList<Params>();

	@GetMapping(value = "/all")
	public Response getScriptInputs(@RequestParam(name = "name", required = true) long name) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		
		return new Response("Done", scriptMasterDao.getScriptInputs(name));
	}

	// @RequestMapping(value = "/user", method = RequestMethod.POST)
	@PostMapping(value = "/execute")
	public Response executeScript(@Valid @RequestBody String scriptName) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		//logger.debug("session id :>>>>>>>>>>>>>>>>>> " + RequestContextHolder.currentRequestAttributes().getSessionId());
		logger.info("=======================================================================");
		logger.info("User :"+ SecurityContextHolder.getContext().getAuthentication().getName());
		logger.info("Script Name :" + scriptName);
		logger.info("=======================================================================");
		String line = null;
		Response response = null;
		StringBuilder stringBuilder = new StringBuilder();
		long start = System.currentTimeMillis();
		try {
			// todo connect through ssh ..
			logger.debug("Process started... :" );
			Process p = Runtime.getRuntime().exec(scriptName);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("<br>");
				// logger.debug(line);
			}
			
		} catch (Exception e) {
			logger.debug(">>ERROR>>" + e.getMessage());
			response = new Response("Done", stringBuilder.append(e.getMessage()));
			//return response;
		}
		logger.info("=======================================================================");
		logger.info("TimeElapsed :: "+ (System.currentTimeMillis() - start) +" milli seconds");
		logger.info("=======================================================================");
		
		
		response = new Response("Done", stringBuilder.toString());

		return response;
	}
	
	@PostMapping(value = "/save")
	public Response UpdateScript(@Valid @RequestBody String updatedvalue) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();   
        String[] values = updatedvalue.split("::@@::");
    	logger.debug("getEntity :>>>>>>>>>>>>>>>>>> 1:"+values[1] + ", 2:"+ values[2] +", 3:"+values[3] + ", 4:"+ values[4]);	
    	ScriptMaster scriptMaster = scriptMasterDao.getScriptsById(new Long(values[1])); 
    	scriptMaster.setScriptDesc(values[3]);
    	scriptMaster.setScriptName(values[2]);
    	scriptMaster.setLocation(values[4]);
    	scriptMaster.setPrefix(values[5]);
    	scriptMaster.setAccess(values[6]);
		logger.debug("query executed and script name is :>>>>>>>>>>>>>>>>>> "+ scriptMaster.getScriptName());
		scriptMasterRepository.save(scriptMaster);		
		Response response = new Response("Done", scriptMaster);
		return response;
	}
	
	@PostMapping(value = "/delete")
	public Response DeleteScript(@Valid @RequestBody String scriptId) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		logger.info("DeleteScript :" + scriptId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			logger.debug("session expired");
			new Response("Done", "session expired. Please login");	
		} 
		scriptMasterDao.DeleteScript(new Long(scriptId));
		Response response = new Response("Done", "Deleted");
		return response;
		
	}
	
	@PostMapping(value = "/addnew")
	public Response AddNewScript(@Valid @RequestBody String updatedvalue) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		logger.info("AddNewScript :" + updatedvalue);
        String[] values = updatedvalue.split("::@@::");
    	
    	logger.debug("getEntity :>>>>>>>>>>>>>>>>>> 1:"+values[0] + ", 2:"+ values[1] +", 3:"+values[2] + ", 4:"+ values[4]+ ", 5:"+ values[5]);	
    	
    	ScriptMaster scriptMaster = new ScriptMaster(); 
    	scriptMaster.setScriptName(values[1]);
    	scriptMaster.setScriptDesc(values[2]);
    	scriptMaster.setLocation(values[3]);
    	scriptMaster.setPrefix(values[4]);
    	scriptMaster.setAccess(values[5]);
		logger.debug("query executed and added script name is :>>>>>>>>>>>>>>>>>> "+ scriptMaster.getScriptName());
		scriptMasterRepository.save(scriptMaster);	
		
		Response response = new Response("Done", scriptMaster);
		return response;
	}
	
	@PostMapping(value = "/updateInputs")
	public Response UpdateScriptInputs(@Valid @RequestBody String updatedvalue) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		logger.debug("update script inputs >>>" + updatedvalue);
        String[] values = updatedvalue.split("::@@::");
    	
        logger.debug("getEntity :>>>>>>>>>>>>>>>>>> 1:"+values[1] + ", 2:"+ values[2] +", 3:"+values[3] +" 4:"+values[4]);	
    	
    	ScriptInputs scriptInputs = scriptMasterDao.getScriptInputsByName(new Long(values[1]),  values[2]); 
    	
    	scriptInputsRepository.deleteByScriptNameAndScriptIdAndInputName( scriptInputs.getScriptName(), scriptInputs.getScriptId(),scriptInputs.getInputName());
    	
    	scriptInputs.setInputName(values[2]);
    	scriptInputs.setInputType(values[3]);
    	scriptInputs.setRequired(values[4]);
    	logger.debug("query executed and script name is :>>>>>>>>>>>>>>>>>> "+ scriptInputs.getScriptId());
     	
    	//scriptInputsRepository.save(scriptInputs);	
    	//scriptMasterDao.persistInput(scriptInputs);
    	//scriptInputsRepository.updateUserSetStatusForName(String inputType, String scriptName, String inputName, long scriptId);
    	//scriptInputsRepository.updateUserSetStatusForName(values[3], values[4], values[2], scriptInputs.getScriptId(), scriptInputs.getScriptName());
    	scriptInputsRepository.save(scriptInputs);
		Response response = new Response("Done", scriptInputs);
		return response;
	}

	@Autowired
	private SessionRegistry sessionRegistry;

	@GetMapping(value = "/onlineusers")
	public Response findAllLoggedInUsers() {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		List<UserDetails> s = sessionRegistry.getAllPrincipals().stream()
				.filter(principal -> principal instanceof UserDetails).map(UserDetails.class::cast)
				.collect(Collectors.toList());

		for (int i = 0; i < s.size(); i++) {
			UserDetails u = s.get(i);
			logger.debug("logged in users " + u.getUsername());
		}
		Response response = new Response("Done", s);
		return response;

	}
	
	public boolean isValidsession() {
		try {
			if (scriptMasterDao.loggedInSession
					.equalsIgnoreCase(RequestContextHolder.currentRequestAttributes().getSessionId())) {
				logger.debug("session alive");
				return true;
			} else {
				logger.debug("session expired");
				return false;
			}
		} catch(java.lang.NullPointerException n) {
			logger.debug("Null.. something wrong in the session.");
			return false;
		} catch(Exception e) {
			logger.debug("Something wrong in the session."+e.getMessage());
			return false;
		} 
	}

}