package com.waitrose.app.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.waitrose.app.entity.ScriptMaster;

public interface ScriptMasterRepository extends CrudRepository<ScriptMaster, Long>  {
	
	List<ScriptMaster> findByAccess(String access);
	
}
