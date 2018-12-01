package com.waitrose.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.waitrose.app.entity.AppUser;
import com.waitrose.app.entity.ScriptInputs;
import com.waitrose.app.entity.ScriptMaster;

@Repository
@Transactional
public class ScriptMasterDao {

	public ScriptMasterDao() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
    private EntityManager entityManager;
 
    public List<ScriptMaster> getScripts(String access) {
        try {
            String sql = "From " + ScriptMaster.class.getName()
            + " e  Where e.access like '%"+ access+ "%'";
            Query query = entityManager.createQuery(sql);
            System.out.println("ScriptMasterDao userRole >>>"+ access);
            List<ScriptMaster> logEntries = query.getResultList(); 
            
            return logEntries;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public ScriptMaster getScriptsById(long id) {
        try {
            String sql = "From " + ScriptMaster.class.getName()
            + " e  Where e.scriptId = "+ id;
            Query query = entityManager.createQuery(sql, ScriptMaster.class);
            System.out.println("ScriptMasterDao getScriptsById >>>"+ id);
            
            return (ScriptMaster) query.getSingleResult();
            
            
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ScriptInputs> getScriptInputs(long scriptId) {
        try {
            String sql = "From ScriptInputs e Where e.scriptId in (:scriptId) ";
  
            Query query = entityManager.createQuery(sql);
            query.setParameter("scriptId", scriptId); 
            List<ScriptInputs> logEntries = query.getResultList(); 
            return logEntries;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Autowired
	private ScriptMasterRepository scriptMasterRepository;
    
    public void UpdateScript(ScriptMaster scriptMaster) {
    	 scriptMasterRepository.save(scriptMaster);	 
    }
    
    public void DeleteScript(Long scriptId) {
   	 scriptMasterRepository.deleteById(scriptId);	 
   }
    
    public List<ScriptInputs> AddScriptInputs(long scriptId) {
        try {
            String sql = "From ScriptInputs e Where e.scriptId in (:scriptId) ";
  
            Query query = entityManager.createQuery(sql);
            query.setParameter("scriptId", scriptId); 
            List<ScriptInputs> logEntries = query.getResultList(); 
            return logEntries;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    
    
    

}
