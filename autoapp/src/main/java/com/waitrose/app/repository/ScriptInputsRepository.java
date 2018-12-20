package com.waitrose.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.waitrose.app.entity.ScriptInputs;

public interface ScriptInputsRepository extends CrudRepository<ScriptInputs, Long>  {
	
	List<ScriptInputs> findByScriptId(Long name);

	void deleteByScriptId(Long scriptId);
   
}
