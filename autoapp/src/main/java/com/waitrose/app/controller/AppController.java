package com.waitrose.app.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;

import com.waitrose.app.entity.AppUser;
import com.waitrose.app.entity.ScriptMaster;
import com.waitrose.app.service.AppServiceImpl;
import com.waitrose.app.service.ManageUserServiceImpl;
import com.waitrose.app.utils.WebUtils;

/**
 * 
 * @author Saravanan
 *
 */
@Controller
public class AppController {

	private static final Logger logger = LoggerFactory.getLogger(AppController.class);

	@Autowired
    private AppServiceImpl appServiceImpl;
	
	@Autowired
    private ManageUserServiceImpl manageUserServiceImpl;
	
	public boolean isValidSession() {
    	return false;
    }	
	@RequestMapping(value = { "/", "/welcome", "/user", "/admin" }, method = RequestMethod.GET)
	public String welcomePage(Model model, Principal principal) {
		System.out.println("================landing page controller==================");
		logger.debug("Stored session id");	
		manageUserServiceImpl.loggedInSession = RequestContextHolder.currentRequestAttributes().getSessionId();
		if (principal == null)
			return "loginPage";
		try {
			// After user login successfully.
			String userName = principal.getName();
			System.out.println("Login user: " + userName);
			User loginedUser = (User) ((Authentication) principal).getPrincipal();
			String userInfo = WebUtils.toString(loginedUser);
			model.addAttribute("userInfo", userInfo);
			List<ScriptMaster> list;
			if (userInfo.contains("ADMIN")) {
				list = appServiceImpl.getScripts("");
			}
			else {
				list = appServiceImpl.getScripts(WebUtils.getRolePrefix(loginedUser));
			}
			
			Collections.reverse(list);
			model.addAttribute("scriptList", list);
			AppUser appUser = appServiceImpl.findUserAccount(userName);
			if (appUser != null) {
				model.addAttribute("lastUsed", appUser.getLastUsed());
			}
			appServiceImpl.updateLastUsed(DateFormat.getInstance().format(new Date()), userName);
			System.out.println("stored login time");
			System.out.println("userInfo>>>>>>>>>>>>>>>>"+ userInfo);
			if (userInfo.contains("ADMIN")) {
				System.out.println("================Forwarding to Admin page==================");	
				return "adminPage";
			} else {
				System.out.println("================Forwarding to User page==================");	
				return "userPage";
			}
		}catch(Exception e) {
			logger.error("Error in executing welcomePage method :" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {
		return "loginPage";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model, Principal principal) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {
		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();
			String userInfo = WebUtils.toString(loginedUser);
			model.addAttribute("userInfo", userInfo);
			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);
		}
		return "403Page";
	}
}