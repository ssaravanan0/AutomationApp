package com.waitrose.app.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.waitrose.app.entity.ScriptInputs;

public interface ScriptInputsRepository extends CrudRepository<ScriptInputs, String>  {
	@Transactional
	@Modifying
	@Query(value = "update script_inputs set input_type=?, required=? where input_name=? and script_id=? and script_name=?", nativeQuery = true) 
	int updateScriptInputsSetInputTypeAndInputName(String inputType, String required, String inputName, long scriptId, String scriptName);
	
	@Modifying
    @Transactional
    @Query(value = "delete from script_inputs where script_name=? and script_id=? and input_name=?", nativeQuery = true)
    void deleteByScriptNameAndScriptIdAndInputName(String ScriptName, long ScriptId, String InputName);

	 
   
}
