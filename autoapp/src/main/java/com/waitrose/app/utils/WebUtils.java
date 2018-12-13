package com.waitrose.app.utils;

import java.util.Collection;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;

import com.waitrose.app.entity.ScriptMaster;
/**
 *  
 * @author Saravanan
 *
 */
public class WebUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
	 
	
    public static String toString(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("UserName:").append(user.getUsername());
 
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            sb.append(" (");
            boolean first = true;
            for (GrantedAuthority a : authorities) {
            	if (first) {
                    sb.append(a.getAuthority()); 
                    first = false;
                } else {
                    sb.append(", ").append(a.getAuthority());
                }
            }
            sb.append(")");
        }
        return sb.toString();
    }
    
    public static String getRolePrefix(User user) {
    	String rolePrefix ="";
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
             for (GrantedAuthority a : authorities) {
             	rolePrefix = rolePrefix + a.getAuthority().charAt(a.getAuthority().indexOf('_')+1);  
            }
        }
       logger.debug("rolePrefix >>>"+ rolePrefix);
        return rolePrefix;
    }
    
    public boolean isValidsession(String loginSession){
		
		if(!loginSession.equalsIgnoreCase(RequestContextHolder.currentRequestAttributes().getSessionId()))
		{
			return false;
		}else {
			return true;
		}
	}
    
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        double d = Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }
}