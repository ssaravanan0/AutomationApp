package com.waitrose.app.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.waitrose.app.entity.AppUser;
import com.waitrose.app.entity.AuditScripts;
import com.waitrose.app.entity.ScriptInputs;
import com.waitrose.app.entity.ScriptMaster;
import com.waitrose.app.entity.UserRole;
import com.waitrose.app.repository.AppUserDAO;
import com.waitrose.app.repository.AppUserRepository;
import com.waitrose.app.repository.AuditScriptsRepository;
import com.waitrose.app.repository.ScriptInputsRepository;
import com.waitrose.app.repository.ScriptMasterRepository;
import com.waitrose.app.repository.UserRoleRepository;


/**
 * 
 * @author Saravanan
 *
 */
@Service
public class AppServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);

	public String loggedInSession;
	
	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private AuditScriptsRepository auditScriptsRepository;
	
	@Autowired
	private UserRoleRepository userRolerRepository;
	
	@Autowired
	private AppUserRepository appUserRepository;

	
	@Autowired
	private ScriptMasterRepository scriptMasterRepository;
	
	@Autowired
	private ScriptInputsRepository scriptInputsRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		logger.debug("Username >> " + userName);
		// todo call the service
		AppUser appUser = appUserRepository.findByUserNameInIgnoreCase(userName.toUpperCase()); //this.appUserDAO.findUserAccount(userName.toUpperCase());

		if (appUser == null) {
			logger.debug("User not found! " + userName);
			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}
 
		logger.debug("Found User: " + appUser);

		// [ROLE_USER, ROLE_ADMIN,..]
		List<String> roleNames = this.appUserDAO.getRoleNames(appUser.getUserId());

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				// ROLE_USER, ROLE_ADMIN,..
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}

		UserDetails userDetails = (UserDetails) new User(appUser.getUserName(), appUser.getEncrytedPassword(),
				grantList);
		//logger.debug("Found User??????????: >>>>>>>>>>>>>>>>>>" + userDetails);
		return userDetails;
	}
	
	public List<UserRole> getUsers(){
		List<UserRole> users = (List<UserRole>) userRolerRepository.findAll();
		//logger.debug(")))))) -> "+users.toString());
		return users;
	}

	public void updateLastUsed(Date d, String userName) {
		appUserRepository.UpdateLastUsed(d, userName);
		//appUserDAO.updateLastUsed(date, userName);
	}

	public AppUser findUserAccount(String userName) {
		return appUserRepository.findByUserName(userName);
	}

}