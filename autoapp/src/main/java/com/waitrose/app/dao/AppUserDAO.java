package com.waitrose.app.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.waitrose.app.entity.AppUser;

/**
 * 
 * @author Saravanan
 *
 */
@Repository
@Transactional
public class AppUserDAO {
 
    @Autowired
    private EntityManager entityManager;
 
    public AppUser findUserAccount(String userName) {
        try {
            String sql = "Select e from " + AppUser.class.getName() + " e " //
                    + " Where e.userName = :userName ";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("userName", userName);
            System.out.println(">>>"+ userName);
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public void updateLastUsed(String d, String userName) {
    	
    	try {
            String sql = "Update App_User set last_used = '"+d+"' where User_Name = '"+userName+"'";
            System.out.println("query is >>>"+ sql);
            Query query = entityManager.createNativeQuery(sql);
            System.out.println("update last used timestamp >>>"+ d);
            query.executeUpdate();
        } catch (NoResultException e) {
        	e.printStackTrace();
        }
    	
    }
    
    
    
    
//    public AppUser save(AppUser appUser) {
//        try {
//            
//            
//        } catch (NoResultException e) {
//            return null;
//        }
//    }
 
}