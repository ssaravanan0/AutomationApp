package com.waitrose.app.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.waitrose.app.dao.AuditScriptsRepository;
import com.waitrose.app.dao.ScriptInputsRepository;
import com.waitrose.app.dao.ScriptMasterDao;
import com.waitrose.app.dao.ScriptMasterRepository;
import com.waitrose.app.entity.AuditScripts;
import com.waitrose.app.entity.ScriptInputs;
import com.waitrose.app.entity.ScriptMaster;
import com.waitrose.app.form.Response;
import com.waitrose.app.service.AppServiceImpl;
import com.waitrose.app.service.ManageUserServiceImpl;


@RestController
@RequestMapping("/api")
public class ManageScriptController {
	
	private static final Logger logger = LoggerFactory.getLogger(ManageScriptController.class);
	
	@Autowired
    private AppServiceImpl appServiceImpl;
	
	@Autowired
    private ManageUserServiceImpl manageUserServiceImpl;

	@Autowired
	private ScriptMasterDao scriptMasterDao;
	
	@Autowired
	private ScriptMasterRepository scriptMasterRepository;
	
	@Autowired
	private ScriptInputsRepository scriptInputsRepository;
	
	@Autowired
	private AuditScriptsRepository auditScriptsRepository;
	
    private long scriptId;
    
	@GetMapping(value = "/findinputs")
	public Response getScriptInputs(@RequestParam(name = "name", required = true) long name) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		logger.info("getScriptInputs"+ name);
		try {
			// for audit purpose 
			scriptId = name;
			List<ScriptInputs> inputs = appServiceImpl.getScriptInputs(name);
			logger.debug("size of inputs : " + inputs.size());
			return new Response("Done", inputs);
		}catch(Exception e) {
			logger.error("Error in executing UpdateScript method :" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping(value = "/findscript")
	public Response findScripts(@RequestParam(name = "param") String param) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		logger.debug("findScripts"+ param);
		try {
			List<ScriptMaster> scriptList;
			if(param.equals("")) {
				scriptList = (List<ScriptMaster>) scriptMasterRepository.findAll();
				//scriptList = scriptMasterRepository.findByAccess(param);
				logger.info(">>>>>>>>>>>>>>>>>find scritps by access : " + scriptList.size());
			}else {
				param = "%"+param+"%"; 
				//scriptList = (List<ScriptMaster>) scriptMasterRepository.findAll();
				scriptList = (List<ScriptMaster>) scriptMasterRepository.findByScriptNameContainingOrScriptDescContainingOrLocationContainingOrPrefixContaining(
						param,param,param,param);
				logger.info(">>>>>>>>>>>>>>>>>find all scritps : " + scriptList.size());
			}
			// for audit purpose
			
			logger.debug("size of inputs : " + scriptList.size());
			return new Response("Done", scriptList);
		}catch(Exception e) {
			logger.error("Error in executing UpdateScript method :" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping(value = "/report")
	public List<AuditScripts> getReport(@RequestParam(name = "scriptid", required = false) String ScriptId, @RequestParam(name = "executedby", required = false) String executedBy, 
			                          @RequestParam(name = "executedon", required = false) String executedOn, @RequestParam(name = "groupid", required = false) String groupId,
			                          @RequestParam(name = "status", required = false) String status) 
	{
		if(!isValidsession()) {
			return null;
		};
		try {
			logger.debug("getReport scriptId"+ ScriptId +", executedBy "+ executedBy +", executedOn "+ executedOn + ",groupId:" + groupId +".status:"+ status );
			List<AuditScripts> auditScripts = appServiceImpl.getReport(ScriptId, executedBy, executedOn, groupId, status);
			logger.debug("size of search results : " + auditScripts.size());
			return auditScripts; //new Response("Done", auditScripts);
		}catch(Exception e) {
			logger.error("Error in executing UpdateScript method :" + e.getMessage());
			e.printStackTrace();
		}
		return null;
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
		logger.info("scriptId :"+ scriptId);
		logger.info("Script Name :" + scriptName);
		logger.info("=======================================================================");
		String line = null;
		Response response = null;
		StringBuilder logs = new StringBuilder();
		AuditScripts auditScripts = new AuditScripts();
		long start = System.currentTimeMillis();
		String status = "Success";
		try {
			// todo connect through ssh ..
			logger.debug("Process started... :" );
			Process p = Runtime.getRuntime().exec(scriptName);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = br.readLine()) != null) {
				logs.append(line);
				//logs.append("<br>");
				//logger.debug(line);
			}
			
		} catch (Exception e) {
			status = "Failed";
			logger.error("Error in executing executeScript method :" + e.getMessage());
			//e.printStackTrace();
			response = new Response("Done", logs.append(e.getMessage()));
		}
		long end = System.currentTimeMillis() - start;
		logger.info("=======================================================================");
		logger.info("TimeElapsed :: "+ end +" milli seconds");
		logger.info("=======================================================================");
		
		auditScripts.setScriptId(scriptId);
		auditScripts.setExecutedBy(SecurityContextHolder.getContext().getAuthentication().getName());
		auditScripts.setScriptName(scriptName);
		auditScripts.setExecutionTime(end);
		auditScripts.setExecutedOn(new Date()+"");
		auditScripts.setStatus(status);
		
		//auditScripts.setExecutedOn(DateFormat.getInstance().format(new Date()));	
		response = new Response("Done", logs.toString());
		auditScriptsRepository.save(auditScripts);
		return response;
	}
	
	@PostMapping(value = "/save")
	public Response updateScript(@Valid @RequestBody String updatedvalue) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		try {
			String[] values = updatedvalue.split("::@@::");
			logger.debug("getEntity :>>>>>>>>>>>>>>>>>> :" + values[1] + ", 2:" + values[2] + ", 3:" + values[3]
					+ ", 4:" + values[4]);
			ScriptMaster scriptMaster = scriptMasterDao.getScriptsById(new Long(values[1]));
			scriptMaster.setScriptDesc(values[3]);
			scriptMaster.setScriptName(values[2]);
			scriptMaster.setLocation(values[4]);
			scriptMaster.setPrefix(values[5]);
			scriptMaster.setAccess(values[6]);
			logger.debug("query executed for script :>>>>>>>>>>>>>>>>>> " + scriptMaster.getScriptName());
			scriptMasterRepository.save(scriptMaster);
			Response response = new Response("Done", scriptMaster);
			return response;
		}catch(Exception e) {
			logger.error("Error in executing UpdateScript method :" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping(value = "/delete")
	public Response DeleteScript(@Valid @RequestBody String scriptId) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		logger.info("DeleteScript :" + scriptId); 
		try {
			scriptMasterDao.DeleteScript(new Long(scriptId));
			Response response = new Response("Done", "Deleted");
			return response;
		}catch(Exception e) {
			logger.error("Error in executing deleteScript method :" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping(value = "/addnew")
	public Response addNewScript(@Valid @RequestBody String updatedvalue) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		logger.info("AddNewScript :" + updatedvalue);
		try {
			String[] values = updatedvalue.split("::@@::");

			logger.info("get parameters:>>>>>>>>>>>>>>>>>> 1:" + values[0] + ", 2:" + values[1] + ", 3:" + values[2]
					+ ", 4:" + values[4] + ", 5:" + values[5]);

			ScriptMaster scriptMaster = new ScriptMaster();
			scriptMaster.setScriptName(values[1]);
			scriptMaster.setScriptDesc(values[2]);
			scriptMaster.setLocation(values[3]);
			scriptMaster.setPrefix(values[4]);
			scriptMaster.setAccess(values[5]);
			logger.debug("query executed and added script name is :>>>>>>>>>>>>>>>>>> " + scriptMaster.getScriptName());
			scriptMasterRepository.save(scriptMaster);

			Response response = new Response("Done", scriptMaster);
			return response;
		}catch(Exception e) {
			logger.error("Error in executing addNewScript method :" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping(value = "/addnewinput")
	public Response addNewScriptInput(@Valid @RequestBody String inputs) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		};
		logger.info("AddNewScript :" + inputs);
		try {
			String[] values = inputs.split("::@@::");
			logger.info("get request contents :>>>>>>>>>>>>>>>>>> 1:"+values[1] + ", 2:"+ values[2] +", 3:"+values[3]+", 4:"+values[4]);	
			ScriptInputs input = new ScriptInputs();
			input.setScriptId(new Long(values[1]));
			input.setScriptName(values[2]);
			input.setInputName(values[3]);
			input.setInputType(values[4]);
			input.setRequired(values[5]);
			//logger.debug("query executed and added script input :>>>>>>>>>>>>>>>>>> " + input.getScriptName());
			scriptInputsRepository.save(input);

			Response response = new Response("Done", input);
			return response;
		}catch(Exception e) {
			logger.error("Error in executing addNewScriptInput method :" + e.getMessage());
			e.printStackTrace();
		}
	return null;
	}
	
	@PostMapping(value = "/updateInputs")
	public Response UpdateScriptInputs(@Valid @RequestBody String updatedvalue) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		}
		try {
			logger.debug("update script inputs >>>" + updatedvalue);
			String[] values = updatedvalue.split("::@@::");
			logger.debug("get request contents : 1:" + values[1] + ", 2:" + values[2] + ", 3:" + values[3]
					+ " 4:" + values[4] + " 5:" + values[5]);
			scriptInputsRepository.updateScriptInputsSetInputTypeAndInputName(values[4], values[5], values[3],
					new Long(values[2]), values[1]);
			Response response = new Response("Done", null);
			return response;	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping(value = "/deleteinput")
	public Response DeleteInput(@Valid @RequestBody String input) {
		if(!isValidsession()) {
			return new Response("Session expired", "Please login");
		}
		if(input == null) {
			logger.info("inputs are null. respond back with error");
			return new Response("Done", "null values not accesspted");
		}
		try {
			logger.info("DeleteInput :" + input);
			String[] values = input.split("::@@::");
			logger.debug("get parameters :>>>>>>>>>>>>>>>>>> 1:" + values[1] + ", 2:" + values[2] + ", 3:" + values[3]);
			scriptInputsRepository.deleteByScriptNameAndScriptIdAndInputName(values[2], new Long(values[1]), values[3]);
			Response response = new Response("Done", "Deleted");
			return response;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;	
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
			if (manageUserServiceImpl.loggedInSession
					.equalsIgnoreCase(RequestContextHolder.currentRequestAttributes().getSessionId())) {
				logger.debug("session alive");
				return true;
			} else {
				logger.debug("session expired");
				return false;
			}
		} catch (java.lang.NullPointerException n) {
			logger.error("Null.. something wrong in the session.");
			return false;
		} catch (Exception e) {
			logger.error("Something wrong in the session." + e.getMessage());
			return false;
		}
	}

}