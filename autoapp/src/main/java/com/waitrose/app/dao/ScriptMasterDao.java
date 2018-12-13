package com.waitrose.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.waitrose.app.entity.ScriptInputs;
import com.waitrose.app.entity.ScriptMaster;


@Repository
@Transactional
public class ScriptMasterDao {

	private static final Logger logger = LoggerFactory.getLogger(ScriptMasterDao.class);

	public ScriptMasterDao() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private EntityManager entityManager;

	public List<ScriptMaster> getScripts(String access) {
		try {
			String sql = "From " + ScriptMaster.class.getName() + " e  Where e.access like '%" + access + "%'";
			Query query = entityManager.createQuery(sql);
			logger.debug("ScriptMasterDao userRole >>>" + access);
			List<ScriptMaster> logEntries = query.getResultList();

			return logEntries;
		} catch (NoResultException e) {
			return null;
		}
	}

	public ScriptMaster getScriptsById(long id) {
		try {
			String sql = "From " + ScriptMaster.class.getName() + " e  Where e.scriptId = " + id;
			Query query = entityManager.createQuery(sql, ScriptMaster.class);
			logger.debug("ScriptMasterDao getScriptsById >>>" + id);

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
			logger.debug("No inputs found for script id" + scriptId);
			return null;
		}
	}

	public ScriptInputs getScriptInputsByName(long scriptId, String scriptName) {
		try {
			String sql = "From ScriptInputs e Where e.scriptId =" + scriptId + " and e.inputName = '" + scriptName
					+ "'";
			;
			Query query = entityManager.createQuery(sql);
			// query.setParameter("scriptId", scriptId);
			// query.setParameter("scriptName", scriptName);
			return (ScriptInputs) query.getSingleResult();

		} catch (NoResultException e) {
			return null;
		}
	}

	public void persistInput(ScriptInputs scriptInputs) {
		try {
			String sql = "ScriptInputs set inputType=:inputType, required=:required where inputName=:inputName and scriptId=:scriptId and scriptName=:scriptName";

			Query query = entityManager.createQuery(sql, ScriptInputs.class);
			/*
			 * Query query1 = entityManager.createQuery(
			 * "DELETE FROM  Seller AS o WHERE o.company=:company AND o.id=:id");
			 */
			query.setParameter("inputType", scriptInputs.getInputType());
			query.setParameter("required", scriptInputs.getRequired());
			query.setParameter("inputName", scriptInputs.getInputName());
			query.setParameter("scriptName", scriptInputs.getScriptName());

			// query.setParameter("id", id);
			int result = query.executeUpdate();

			// query.executeUpdate();
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>> store updated inputs" + result);
			/**
			 * Query query = Seller.entityManager().createQuery( "DELETE FROM Seller AS o
			 * WHERE o.company=:company AND o.id=:id"); query.setParameter("company",
			 * company); query.setParameter("id", id); int result = query.executeUpdate();
			 */
			// entityManager.persist(scriptInputs);

		} catch (NoResultException e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private ScriptMasterRepository scriptMasterRepository;

	@Autowired
	private ScriptInputsRepository scriptInputsRepository;
	
	public void UpdateScript(ScriptMaster scriptMaster) {
		scriptMasterRepository.save(scriptMaster);
	}

	public void DeleteScript(Long scriptId) {
		scriptMasterRepository.deleteById(scriptId);
	}

	public List<ScriptInputs> AddScriptInputs(long scriptId, String name) {
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
	
	public void DeleteScriptInput(Long scriptId) {
		scriptInputsRepository.delete(getScriptInputsByName(scriptId, ""));
	}

}
