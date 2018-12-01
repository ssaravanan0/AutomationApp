package com.waitrose.app.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;

import com.waitrose.app.dao.AppUserDAO;
import com.waitrose.app.dao.ScriptMasterDao;
import com.waitrose.app.entity.AppUser;
import com.waitrose.app.entity.ScriptMaster;
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
	private ScriptMasterDao scriptMasterDao;

	@Autowired
	private AppUserDAO appUserDAO = new AppUserDAO();
	 
    
	public boolean isValidSession() {
    	return false;
    }
	
	@RequestMapping(value = { "/", "/welcome", "/user", "/admin" }, method = RequestMethod.GET)
	public String welcomePage(Model model, Principal principal) {
		logger.info("landing page controller");
		logger.debug("stored the session id :>>>>>>>>>>>>>>>>>> "+ RequestContextHolder.currentRequestAttributes().getSessionId());	
		scriptMasterDao.loggedInSession = RequestContextHolder.currentRequestAttributes().getSessionId();
		
		if (principal == null)
			return "loginPage";

		// After user login successfully.
		String userName = principal.getName();
		logger.info("Login user: " + userName);
		
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);	 
		List<ScriptMaster> list = scriptMasterDao.getScripts(WebUtils.getRolePrefix(loginedUser));
		Collections.reverse(list);
		model.addAttribute("scriptList", list);
		
		AppUser appUser = this.appUserDAO.findUserAccount(userName);
        if (appUser != null) {
            model.addAttribute("lastUsed", appUser.getLastUsed());
        }
        
        Date date = new Date();
		appUserDAO.updateLastUsed(DateFormat.getInstance().format(date), userName);
		logger.info("stored login time");

		if (userInfo.contains("ADMIN")) {
			logger.info("Admin user");
			return "adminPage";
		} else {
			logger.info("Normal user");
			return "userPage";
		}
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
	
	@RequestMapping(value="/logoutuser", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {

		logger.info("logoutPage:>>>>>>>>>>>>>>>>>> ");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			Date date = new Date();
			appUserDAO.updateLastUsed(DateFormat.getInstance().format(date), auth.getName());
			new SecurityContextLogoutHandler().logout(request, response, auth);

		}
		return "loginPage";
	}

}