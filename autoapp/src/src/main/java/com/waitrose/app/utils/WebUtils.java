package com.waitrose.app.utils;

import java.util.Collection;
import java.util.StringTokenizer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.waitrose.app.entity.ScriptMaster;
/**
 *  
 * @author Saravanan
 *
 */
public class WebUtils {
	
	public static final String ROLE_GROUP_USER = "ROLE_USER";
	public static final String ROLE_GROUP_ADMIN ="ROLE_ADMIN";
	public static final String ROLE_GROUP_A = "A";
	public static final String ROLE_GROUP_U = "U";
	
	public static String sId = null;
	
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
     
    
    public static String validateRole(String userInfo) {
    	//String userInfo = toString(user);
    	System.out.println("validateRole:>>>>>>>>>>>>>>>>>> "+userInfo);	
    	StringBuilder role = new StringBuilder();
    	if(userInfo.contains(ROLE_GROUP_ADMIN)) {
     	   role.append(ROLE_GROUP_A);
     	   System.out.println("admin page:>>>>>>>>>>>>>>>>>> ");	
     	   
        }
        else if (userInfo.contains(ROLE_GROUP_USER)) {
     	   role.append(ROLE_GROUP_U);
     	   System.out.println("user page:>>>>>>>>>>>>>>>>>> ");	
     	   
        }
    	System.out.println("role>>>>>" + role.toString());
        return role.toString();
    }
     
}