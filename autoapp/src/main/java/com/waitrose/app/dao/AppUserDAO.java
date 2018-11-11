package com.waitrose.app.dao;

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
 
}