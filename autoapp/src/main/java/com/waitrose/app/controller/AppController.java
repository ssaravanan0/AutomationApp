package com.waitrose.app.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.waitrose.app.form.Params;
import com.waitrose.app.utils.WebUtils;

/**
 *  
 * @author Saravanan
 *
 */
@Controller
public class AppController {
 
    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }
 
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
         
        return "adminPage";
    }
 
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
 
        return "loginPage";
    }
 
    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }
 
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
 
        // After user login successfully.
        String userName = principal.getName();
 
        System.out.println("User Name: " + userName);
 
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
 
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
 
        return "userPage";
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
    
    @RequestMapping(value = "/callscript", method = RequestMethod.POST)
    public String callScript(Model model, Principal principal) {
    	if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();
            String userInfo = WebUtils.toString(loginedUser);
        System.out.println("CallScript>>>>>>>>>>>>");
        String message = "Hi " + principal.getName() //
        + "<br> successfully executed the shell script!";
        
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("message", message);
    	}       
 
        return "adminPage";
    }
    
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String createNewUser(@Valid Params params, BindingResult bindingResult, Model model) {
    	
    	System.out.print(">>>"+  params.getLastName()+ ">>"+ params.getName());
    	
    	if (bindingResult.hasErrors()) {
			return "userPage";
		}else {
			
			model.addAttribute("successMessage", "Shell script has been successfully executed");
		
		}
    	
    	return "userPage";
    }
    
    @ModelAttribute("params")
    public Params getParamsObject() {
     return new Params();
    }
 
 
}