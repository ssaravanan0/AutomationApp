package com.waitrose.app.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.print.attribute.standard.Sides;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.waitrose.app.dao.ScriptMasterDao;
import com.waitrose.app.dao.ScriptMasterRepository;
import com.waitrose.app.entity.ScriptInputs;
import com.waitrose.app.entity.ScriptMaster;
import com.waitrose.app.form.Params;
import com.waitrose.app.form.Response;
import com.waitrose.app.utils.WebUtils;

@RestController
@RequestMapping("/api")
public class RestWebController {

	@Autowired
	private ScriptMasterDao scriptMasterDao;
	
	@Autowired
	private ScriptMasterRepository scriptMasterRepository;

	List<Params> cust = new ArrayList<Params>();

	@GetMapping(value = "/all")
	public Response getScriptInputs(@RequestParam(name = "name", required = true) long name) {

		System.out.println(">>>>getScriptInputs" + name);
		System.out.println(
				"session id :>>>>>>>>>>>>>>>>>> " + RequestContextHolder.currentRequestAttributes().getSessionId());
		
		if(WebUtils.sId.equals(RequestContextHolder.currentRequestAttributes().getSessionId())){
			System.out.println(
					"session id same " );
		}else{
			System.out.println(
					"session id Notttttt same " );
		};

		List<ScriptInputs> l = scriptMasterDao.getScriptInputs(name);
		System.out.println(">>>>>>>" + l.size());
		Response response = new Response("Done", l);

		return response;
	}

	// @RequestMapping(value = "/user", method = RequestMethod.POST)
	@PostMapping(value = "/execute")
	public Response executeScript(@Valid @RequestBody String scriptName) {

		System.out.print("Execute script >>>" + scriptName);
		System.out.println(
				"session id :>>>>>>>>>>>>>>>>>> " + RequestContextHolder.currentRequestAttributes().getSessionId());
        
		if(WebUtils.sId.equals(RequestContextHolder.currentRequestAttributes().getSessionId())){
			System.out.println(
					"session id same " );
		}else{
			System.out.println(
					"session id Notttttt same " );
		};
				
		String line = null;
		Response response = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			// todo connect through ssh ..
			Process p = Runtime.getRuntime().exec(scriptName);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("<br>");
				// System.out.println(line);
			}

		} catch (Exception e) {
			System.out.println(">>ERROR>>" + e.getMessage());
			response = new Response("Done", e.getMessage());
			return response;
		}
		response = new Response("Done", stringBuilder.toString());

		return response;
	}
	
	@PostMapping(value = "/save")
	public Response UpdateScript(@Valid @RequestBody String updatedvalue) {

		System.out.print("save script >>>" + updatedvalue);
		
		System.out.println(
				"session id :>>>>>>>>>>>>>>>>>> " + RequestContextHolder.currentRequestAttributes().getSessionId());
		if(WebUtils.sId.equals(RequestContextHolder.currentRequestAttributes().getSessionId())){
			System.out.println(
					"session id same " );
		}else{
			System.out.println(
					"session id Notttttt same " );
		};
		
        String[] values = updatedvalue.split("::@@::");
    	
    	System.out.println("getEntity :>>>>>>>>>>>>>>>>>> 1:"+values[1] + ", 2:"+ values[2] +", 3:"+values[3] + ", 4:"+ values[4]);	
    	
    	ScriptMaster scriptMaster = scriptMasterDao.getScriptsById(new Long(values[1])); 
    	scriptMaster.setScriptName(values[2]);
    	scriptMaster.setScriptDesc(values[3]);
    	scriptMaster.setLocation(values[4]);
    	scriptMaster.setPrefix(values[5]);
    	scriptMaster.setAccess(values[6]);
    	
		System.out.println("query executed and script name is :>>>>>>>>>>>>>>>>>> "+ scriptMaster.getScriptName());
		scriptMasterRepository.save(scriptMaster);	
		
		Response response = new Response("Done", scriptMaster);
		return response;
	}
	
	@PostMapping(value = "/delete")
	public Response DeleteScript(@Valid @RequestBody String scriptId) {
		System.out.print("DeleteScript >>>" + scriptId);
		if(WebUtils.sId.equals(RequestContextHolder.currentRequestAttributes().getSessionId())){
			System.out.println(
					"session id same " );
		}else{
			System.out.println(
					"session id Notttttt same " );
		};
		//scriptMasterDao.DeleteScript(new Long(scriptId));
		Response response = new Response("Done", "Deleted");
		return response;
		
	}
	
	@PostMapping(value = "/addnew")
	public Response AddNewScript(@Valid @RequestBody String updatedvalue) {

		System.out.print(" :: AddNewScript :" + updatedvalue + "::");
		System.out.println(
				"session id :>>>>>>>>>>>>>>>>>> " + RequestContextHolder.currentRequestAttributes().getSessionId());
		if(WebUtils.sId.equals(RequestContextHolder.currentRequestAttributes().getSessionId())){
			System.out.println(
					"session id same " );
		}else{
			System.out.println(
					"session id Notttttt same " );
		};
        String[] values = updatedvalue.split("::@@::");
    	
    	System.out.println("getEntity :>>>>>>>>>>>>>>>>>> 1:"+values[1] + ", 2:"+ values[2] +", 3:"+values[3] + ", 4:"+ values[4]);	
    	
    	ScriptMaster scriptMaster = new ScriptMaster(); 
    	scriptMaster.setScriptName(values[1]);
    	scriptMaster.setScriptDesc(values[2]);
    	scriptMaster.setLocation(values[3]);
    	scriptMaster.setPrefix(values[4]);
    	scriptMaster.setAccess(values[5]);
		System.out.println("query executed and script name is :>>>>>>>>>>>>>>>>>> "+ scriptMaster.getScriptName());
		scriptMasterRepository.save(scriptMaster);	
		
		Response response = new Response("Done", scriptMaster);
		return response;
	}
	
	

	@Autowired
	private SessionRegistry sessionRegistry;

	@GetMapping(value = "/onlineusers")
	public Response findAllLoggedInUsers() {
		List<UserDetails> s = sessionRegistry.getAllPrincipals().stream()
				.filter(principal -> principal instanceof UserDetails).map(UserDetails.class::cast)
				.collect(Collectors.toList());

		for (int i = 0; i < s.size(); i++) {
			UserDetails u = s.get(i);
			System.out.println("logged in users " + u.getUsername());
		}
		Response response = new Response("Done", s);
		return response;

	}

}