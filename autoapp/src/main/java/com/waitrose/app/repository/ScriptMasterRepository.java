package com.waitrose.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.waitrose.app.entity.ScriptMaster;

public interface ScriptMasterRepository extends CrudRepository<ScriptMaster, Long>  {
	
	List<ScriptMaster> findByAccess(String access);
	
	List<ScriptMaster> findByScriptNameContainingOrScriptDescContainingOrLocationContainingOrPrefixContaining(
			  String scriptName,String scriptDesc,String location,String prefix);
	
}
