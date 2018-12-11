package com.waitrose.app.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.waitrose.app.controller.ManageScriptController;
/**
 *  
 * @author Saravanan
 *
 */
public class EncrytedPasswordUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(EncrytedPasswordUtils.class);
 
    // Encryte Password with BCryptPasswordEncoder
    public static String encrytePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    
    public static void main(String[] args) {
        String password = "123";
        String encrytedPassword = encrytePassword(password);
 
       logger.debug("Encryted Password: " + encrytedPassword);
    }
     
}