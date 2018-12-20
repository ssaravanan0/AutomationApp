package com.waitrose.app.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.waitrose.app.repository.AppUserDAO;

@Service
public class UserService 
//implements ApplicationListener<AuthenticationSuccessEvent>
{

	@Autowired
	private AppUserDAO appUserDAO = new AppUserDAO();
	
//	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		
		//event.getAuthentication().getName()
		
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String username = event.getAuthentication().getName(); //auth.getName();
//		
//		System.out.println("UserService ::: :::::::" );
//		System.out.println("UserService ::: username >>>>" +username );
//		System.out.println("UserService ::: :::::::" );
		if (username != null) {
			Date date = new Date();
			//appUserDAO.updateLastUsed(date, username);
			
			}else {
				System.out.println("Principal is null:>>>>>>>>>>>>>>>>>> ");
			}
		//String userName = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
		//User user = this.userDao.findByLogin(userName);
		//user.setLastLoginDate(new Date());
	}
}