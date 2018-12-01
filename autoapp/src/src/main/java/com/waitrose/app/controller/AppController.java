package com.waitrose.app.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.waitrose.app.dao.AppUserDAO;
import com.waitrose.app.dao.ScriptMasterDao;
import com.waitrose.app.entity.AppUser;
import com.waitrose.app.entity.ScriptMaster;
import com.waitrose.app.form.Params;
import com.waitrose.app.utils.WebUtils;

/**
 * 
 * @author Saravanan
 *
 */
@Controller
public class AppController {

	@Autowired
	private ScriptMasterDao scriptMasterDao;

	@Autowired
	private AppUserDAO appUserDAO = new AppUserDAO();
	
	
	
	@RequestMapping(value = { "/", "/welcome", "/user", "/admin" }, method = RequestMethod.GET)
	public String welcomePage(Model model, Principal principal, HttpSession session) {
		System.out.println("welcome page:>>>>>>>>>>>>>>>>>> ");
		System.out.println("session id :>>>>>>>>>>>>>>>>>> "+ RequestContextHolder.currentRequestAttributes().getSessionId());
		
		WebUtils.sId = RequestContextHolder.currentRequestAttributes().getSessionId();
		session.setAttribute("appId", RequestContextHolder.currentRequestAttributes().getSessionId());
		
		
		if (principal == null)
			return "loginPage";

		// After user login successfully.
		String userName = principal.getName();
		System.out.println("User Name: " + userName);
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);
		String s = WebUtils.validateRole(userInfo);
		List<ScriptMaster> list = scriptMasterDao.getScripts(s);
		model.addAttribute("scriptList", list);
		
		AppUser appUser = this.appUserDAO.findUserAccount(userName);
		 //todo need to fin
        if (appUser != null) {
            System.out.println("User found! " + userName);
            model.addAttribute("lastUsed", appUser.getLastUsed());
            //throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }

		if (s.contains("A")) {
			System.out.println("admin page:>>>>>>>>>>>>>>>>>> ");
			return "adminPage";
		} else if (s.contains("U")) {
			System.out.println("user page:>>>>>>>>>>>>>>>>>> ");
			return "userPage";
		}

		return "welcomePage";
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
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("logoutPage:>>>>>>>>>>>>>>>>>> ");
		
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    
	    if (auth != null){    
	       
	        
           Date date = new Date();
			
			//DateFormat.getInstance().format(date);
			
			//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("CREATION_TIME"));
			
           appUserDAO.updateLastUsed(DateFormat.getInstance().format(date), auth.getName());
           
           new SecurityContextLogoutHandler().logout(request, response, auth);
			
	    }
	    return "loginPage";
	}

	@ModelAttribute("params")
	public Params getParamsObject() {
		return new Params();
	}

}