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

	public List<AuditScripts> getReport(String ScriptId, String executedBy, String executedOn, String groupId, String status) {
        boolean isFilter = false;
		AuditScripts filter = new AuditScripts();
		if(ScriptId != null && !ScriptId.equals("")) {
			logger.debug("ScriptId:"+ScriptId);
			filter.setScriptId(new Long(ScriptId));
			isFilter = true;
		}
		if(executedBy!= null && !executedBy.equals("")) {
			logger.debug("executedBy:"+executedBy);
			filter.setExecutedBy(executedBy);
			isFilter = true;
		}
		if(executedOn != null) {
			logger.debug("executedOn:"+executedOn); 
			//filter.setExecutedOn(executedOn);
			isFilter = true;
		}
		if(groupId!= null && !groupId.equals("")) {
			logger.debug("groupId:"+groupId);
			filter.setGroupId(groupId);
			isFilter = true;
		}
		
		if(status!= null && !status.equals("")) {
			logger.debug("status:"+status);
			filter.setStatus(status);
			isFilter = true;
		}
		
		List<AuditScripts> result = null;
		
		if(isFilter) {
			Specification<AuditScripts> spec = new AuditScriptSpecification(filter);
			result = auditScriptsRepository.findAll(spec);
		}else {
			//a week report
			Calendar cal = new GregorianCalendar();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			Date sevenDaysAgo = cal.getTime();
			
			Date now = new Date();
			
			result = (List<AuditScripts>) auditScriptsRepository.findAll();
			result = auditScriptsRepository.findAllByExecutedOnBetween(sevenDaysAgo, now);
		}
		
		return result; 
	}

}