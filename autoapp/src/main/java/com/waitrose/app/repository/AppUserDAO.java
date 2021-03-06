package com.waitrose.app.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.waitrose.app.entity.AppUser;
import com.waitrose.app.entity.UserRole;

/**
 * 
 * @author Saravanan
 *
 */
@Repository
@Transactional
public class AppUserDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(AppUserDAO.class);
 
    @Autowired
    private EntityManager entityManager;
 
    public AppUser findUserAccount(String userName) {
        try {
            String sql = "Select e from " + AppUser.class.getName() + " e " //
                    + " Where e.userName = :userName ";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userName", userName);
           logger.debug(">>>"+ userName);
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public AppUser getUsers() {
        try {
            String sql = "Select e from " + AppUser.class.getName() + " e " //
                    + " Where e.userName = :userName ";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
           
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public void updateLastUsed(String d, String userName) {
    	
    	try {
            String sql = "Update App_User set last_used = '"+d+"' where User_Name = '"+userName+"'";
           logger.debug("query is >>>"+ sql);
            Query query = entityManager.createNativeQuery(sql);
           logger.debug("update last used timestamp >>>"+ d);
            query.executeUpdate();
        } catch (NoResultException e) {
        	e.printStackTrace();
        }
    	
    }
    
    public List<String> getRoleNames(Long userId) {
        String sql = "Select ur.appRole.roleName from " + UserRole.class.getName() + " ur " //
                + " where ur.appUser.userId = :userId ";
 
        Query query = this.entityManager.createQuery(sql, String.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
 
}