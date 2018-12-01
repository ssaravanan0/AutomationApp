package com.waitrose.app.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.waitrose.app.entity.ScriptInputs;

public interface ScriptInputsRepository extends CrudRepository<ScriptInputs, String>  {
	@Transactional
	@Modifying
	//@Query(value = "update ScriptInputs set inputType=?, required=? where inputName=? and scriptId=? and scriptName =?", nativeQuery = true) 
	@Query(value = "update script_inputs set input_type=?, required=? where input_name=? and script_id=? and script_name=?", nativeQuery = true) 
	int updateUserSetStatusForName(String inputType, String required, String inputName, long scriptId, String scriptName);
	
	//@Transactional    
	//@Modifying
	//@Query(value="DELETE FROM script_inputs WHERE input_name=? and script_id=? and script_name=?", nativeQuery = true); 
	//void deleteByStart_dateAndUsernameAndEnd_date(Date start_date,String username,Date end_date);
	
	@Modifying
    @Transactional
    void deleteByScriptNameAndScriptIdAndInputName(String ScriptName, long ScriptId, String InputName);
	 
   
}
