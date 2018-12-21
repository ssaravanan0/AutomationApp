$( document ).ready(function() {
	
	$("#main").hide();
	//$("#scriptlistDiv").show(500);
	var trid;
	var scriptName;
	
     //admin page
	var currentValue ="";
	var updatedvalue = "";
	var saparator = "::@@::";
	var usrAndRoles;
	
	var gotRoles = false;
	if(!gotRoles)
	 {
		 getRoles();
		 gotRoles = true;
	 }
		
	var requestRunning = false;
	$(".reports").click(function(event) {
		$('#addRemoveUsersDiv').hide();
		$('#addRemoveInputsDiv').hide();
		$('#scriptlistDiv').hide();
		$('#addRemoveGroupsDiv').hide();
		$('#postResultDiv').hide();
		$('#postResultDiv1').hide();
		$('#auditResultDiv').show();
		$('#reportsDiv').show();
		$('#footer').hide();
		if (!requestRunning) { 
			// don't do anything if already data loaded
			var table = $('#auditTable').DataTable({
				"sAjaxSource": "/report/weeklyreport" ,
				"sAjaxDataProp": "",
				"order": [[ 0, "asc" ]],
				"aoColumns": [
				      { "mData": "scriptId"},
			          { "mData": "scriptName" },
					  { "mData": "executedOn" },
					  { "mData": "executionTime" },
					  { "mData": "status" },
					  { "mData": "executedBy" }				 
				]
	      })
		}
	 $("#auditTable").show();
	 $('#reportsDiv').show();
	 
	 requestRunning = true;
	});
	
	$(".manageScripts").click(function(event) {
		$('#addRemoveInputsDiv').hide();
		$('#addRemoveUsersDiv').hide();
		$('#scriptlistDiv').show();
		$('#addRemoveGroupsDiv').hide();
		$('#postResultDiv').hide();
		$('#reportsDiv').hide();
		$("#auditResultDiv").hide();
		getRoles();
		getScripts("");
		$("#manageScriptDiv").show();
		$('#footer').show();
		
	});
	
	$(".manageScriptInputs").click(function(event) {
		$('#addRemoveUsersDiv').hide();
		$('#addRemoveInputsDiv').show();
		$('#scriptlistDiv').hide();
		$('#addRemoveGroupsDiv').hide();
		$('#postResultDiv').hide();
		$('#postResultDiv1').hide();
		$('#reportsDiv').hide();
		$("#auditResultDiv").hide();
		$('#footer').show();
	});
		 
	$(".manageGroup").click(function(event) {
		$('#addRemoveGroupsDiv').show();
		$('#addRemoveUsersDiv').hide();
		$('#addRemoveInputsDiv').hide();
		$('#scriptlistDiv').hide();	 
		$('#postResultDiv').hide();
		$('#auditResultDiv').hide();
		$('#footer').show();
		getRoles();
	});
	
	$(document).on("click", ".cancelAddNewGroup", function(){
		 //alert("cancel new group");
		 $(this).parents("tr").remove();
		 $(".add-new-group").removeAttr("disabled");
		});

	$(document).on("click", ".cancelrole", function(){ 
    	//alert("You have clicked the cancel");
    	event.preventDefault();
    	$(this).parents("tr").find(".editrole, .removerole").show();
    	var input = $(this).parents("tr").find('input[type="text"]');
    	input.each(function(){    	
    		$(this).parent("td").html($(this).val())
    	});
    	$(this).parents("tr").find('td').each(
    			function (i) {
    				if(i==2){
    					$(this).html(selectedDropdownRole);
    				}
    	});
    	$(this).parents("tr").find(".saverole").hide();
    	$(this).parents("tr").find(".cancelrole").hide();
	});
	//add new user
	$(document).on("click", ".add-new-group", function(){
			//alert("add new group >>");
	    	$(this).attr("disabled", "disabled");
			var index = $("table-inputs tbody tr:last-child").index();
			$("#roleTbl").show();
			var row = '<tr>' +
	            '<td class="filterable-cell"></td>' +
	            '<td class="filterable-cell"><input type="text" class="form-control" name="gname" id="gname"></td>' +
	            '<td class="filterable-cell"><input type="text" class="form-control" name="gprefix" id="gprefix"></td>' +
	            '<td class="filterable-cell"><a href="#" class="addNewGroup"> Save </a><a href="#" class="cancelAddNewGroup"> Cancel </a></td>' +
	            '</tr>';
			$("#roleTbl").append(row);
			
	    	$(row).insertBefore('table table-inputs > tbody > tr:first');
			$("table table-inputs tbody tr").eq(index + 1).find(".add, .edit").toggle();
	});
	
	var IsRoleRequested = false;
	$(".manageUsers").click(function(event) {
		$('#addRemoveInputsDiv').hide();
		$('#addRemoveUsersDiv').show();
		$('#scriptlistDiv').hide();
		$('#addRemoveGroupsDiv').hide();
		$('#postResultDiv').hide();
		$('#postResultDiv1').hide();
		$('#reportsDiv').hide();
		$("#auditResultDiv").hide();
		$('#footer').show();
		
		IsRoleRequested = false;
		if(!IsRoleRequested){
			getRolesForSearchDropdown();
			IsRoleRequested = true;
		}
		
	});
	var searchScriptParam="";
	$("#script-search-btn").click(function(event) {
		//alert("--"+$("#script-search").val());
		searchScriptParam = $("#script-search").val()
		getScripts(searchScriptParam);
		$("#manageScripts").show();
	});
	
	function getScripts(param){
		//alert(param)
		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : "/api/findscript?param="+param,
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					//roleList = result.data;
					$("#manageScripts td").remove();
					if(result.data.length == 0){
						$("#manageScripts").append("<tr><td style='width:50%' >No matching records found</td></tr>");
					}
					//$("#selectGroup").empty();
					$.each(result.data, function(i){
						//alert(result.data[i].location);
						$("#manageScripts").append('<tr id='+result.data[i].location + result.data[i].scriptName+'.'+result.data[i].prefix+'>'+     
				        '<td class="filterable-cell">'+result.data[i].scriptId+'</td>'+
				        '<td class="filterable-cell">'+result.data[i].scriptName+'</td>'+
				        '<td class="filterable-cell">'+result.data[i].scriptDesc+'</td>'+
				        '<td class="filterable-cell">'+result.data[i].location+'</td>'+
				        '<td class="filterable-cell">'+result.data[i].prefix+'</td>'+
				        '<td class="filterable-cell">'+result.data[i].access+'</td>'+
				        '<td class="filterable-cell" ><a href="#" id='+result.data[i].scriptId+' class=run>run</a></td>'+
				        '<td class="filterable-cell"> <a href="#" id='+ result.data[i].scriptId+' class="editscript"> Edit </a> <a href="#" id='+result.data[i].scriptId+'  class="removescript" >Remove</a>'+ 
				        '<a href="#" id='+ result.data[i].scriptId +' class="updatescript" style="display: none;" > Save </a><a href="#" id='+result.data[i].scriptId+' class="cancelupdatescript" style="display: none;"> Cancel </a></td>');
				        '</tr>' ;
						
					});
 				}else if(result.status == "Session expired"){
					location.reload();
				}
				console.log(result);
			},
			error : function(e) {
				//alert("Error!"+ e)
			}
		});
	}
	
	var roleList;
	function getRoles(){
		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : "/group/getgroups",
			data : updatedvalue,
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					roleList = result.data;
					$("#roleTbl td").remove();
					//$("#selectGroup").empty();
					$.each(result.data, function(i){	
						$("#roleTbl").append('<tr><td>'+result.data[i].roleId+'</td><td>'+result.data[i].roleName+'</td><td>'+result.data[i].rolePrefix+'</td>'
								+'<td><a href="#" class="editrole"> Edit </a> <a href="#"  class="removerole" >Remove</a>'
				    			+'<a href="#" class="saverole" style="display: none;" > Save </a><a href="#" class="cancelrole" style="display: none;"> Cancel </a></td></tr>');
					});
 				}else if(result.status == "Session expired"){
					location.reload();
				}
				console.log(result);
			},
			error : function(e) {
				//alert("Error!"+ e)
			}
		});
	}
	
	function getRolesForSearchDropdown(){
		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : "/group/getgroups",
			data : updatedvalue,
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){
					roleList = result.data;
					$.each(result.data, function(i){
						$("#selectGroup").append('<option value='+result.data[i].roleId+'>'+result.data[i].roleName+'</option>');
					});
					var opt = {};
				    $("#selectGroup > option").each(function () {
				        if(opt[$(this).text()]) {
				            $(this).remove();
				        } else {
				            opt[$(this).text()] = $(this).val();
				        }
				    });
 				}else if(result.status == "Session expired"){
					location.reload();
				}
				console.log(result);
			},
			error : function(e) {
				//alert("Error!"+ e)
			}
		});
	}
	
	function getUsers(isDropDownRefresh){
		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : "/user/getusers" ,
			//data : scriptid=,
			dataType : 'json',
			success : function(result) {
				if(result.status == "Done"){ 
					usrAndRoles = result; 
						if(isDropDownRefresh == true){
					        //alert("You have selected the role - " + selectedRole);
					        $("#userTbl td").remove();
							$("#userTbl").show();
					    	$.each(result.data, function(i){
					    		if(result.data[i].appRole.roleId == selectedRole){
					    			$("#userTbl").append('<tr><td>'+result.data[i].appUser.userId+'</td><td>'+result.data[i].appUser.userName+'</td><td>'+result.data[i].appRole.roleName+'</td><td>'+result.data[i].appUser.enabled+'</td>'
					    					+'<td><a href="#" class="edituser"> Edit </a> <a href="#"  class="removeuser" >Remove</a>'
					    			+'<a href="#" class="saveuser" style="display: none;" > Save </a><a href="#" class="canceluser" style="display: none;"> Cancel </a></td></tr>');
					    		}// 
					    	});	
						} 			 
 				}else if(result.status == "Session expired"){
					location.reload();
				} 
				console.log(result);
			},
			error : function(e) {
				alert("Error!"+ e)
			}
		});
	}
	
	$(document).on("click", ".saverole", function(){
		 //alert("saveuser");
		 $(".add-new-group").removeAttr("disabled");
		 
		    var empty = false;
			var input = $(this).parents("tr").find('input[type="text"]', 'option:selected');
			
			input.each(function(){
				if(!$(this).val()){
					$(this).addClass("error")
					empty = true;
					//alert("empty")
				} else{
		         $(this).removeClass("error");
		     }
			});
			
			$(this).parents("tr").find(".error").first().focus();
			//alert("currentValue :"+currentValue);
			var postContent ;
			if(!empty){
				updatedvalue = saparator + $(this).parent().siblings(":first").text();
				var col2=""; 
		        var col3="";
				input.each(function(i){
    				if(i==0){
    					col2 = $(this).val();
    				}
    				if(i==1){
    					col3 = $(this).val();
    				}
				});
		        updatedvalue = updatedvalue + saparator + col2 + saparator + col3 ;
				//alert("after changing:"+ updatedvalue);
			}
			
			if(updatedvalue == currentValue)
			{
			 //alert("both are same");
			}else {
			  //alert("both are different .. update it in db");
				
				$(".add-new-user").removeAttr("disabled");
				//alert(">>" + newscriptcontent)
				$.ajax({
					type : "PUT",
					contentType : "application/json",
					url : "/group/updategroup",
					data : updatedvalue,
					dataType : 'json',
					success : function(result) {
						if(result.status == "Done"){
							//showResults();
							getRoles();
							//showUsers();
							
		 				}else if(result.status == "Session expired"){
							location.reload();
						}
						console.log(result);
					},
					error : function(e) {
						alert("Error!"+ e)
					}
				});
			}
		});
	
	$(document).on("click", ".saveuser", function(){
		 //alert("saveuser"+ $('#1').text());
		 $(".add-new-user").removeAttr("disabled");
		    var empty = false;
			var input = $(this).parents("tr").find('input[type="text"]', 'option:selected');	
			input.each(function(){
				if(!$(this).val()){
					$(this).addClass("error")
					empty = true;
					//alert("empty")
				} else{
		         $(this).removeClass("error");
		     }
			});
			
			$(this).parents("tr").find(".error").first().focus();
			//alert("currentValue"+currentValue);
			var postContent ;
			if(!empty){
				var col2="";//username
				var col4="";//isActive
				input.each(function(i){
		    				if(i==0){
		    					col2 = $(this).val();
		    				}
		    				if(i==1){
		    					col4 = $(this).val();
		    				}
		    	});
				
				updatedvalue = saparator + $(this).parent().siblings(":first").text();
				//var col2=$('#1').val(); // get current row 2nd TD
		        var col3=$('.updaterole').find(":selected").val();  // get current row 3rd TD
		        // var col4=$('#3').val();
		        updatedvalue = updatedvalue + saparator + col2 + saparator + col3 + saparator + col4 
				//alert("after changing:"+ updatedvalue);
			}
			
			if(updatedvalue == currentValue)
			{
			 //alert("both are same");
			}else {
			  //alert("both are different .. update it in db");				
				$(".add-new-user").removeAttr("disabled");
				//alert(">>" + newscriptcontent)
				$.ajax({
					type : "PUT",
					contentType : "application/json",
					url : "/user/updateuser",
					data : updatedvalue,
					dataType : 'json',
					success : function(result) {
						if(result.status == "Done"){
							//showResults();
							getUsers(true);
							//showUsers();
							
		 				}else if(result.status == "Session expired"){
							location.reload();
						}
						console.log(result);
					},
					error : function(e) {
						alert("Error!"+ e)
					}
				});
			}
		});
	
	$(document).on("click", ".addNewGroup", function(){
		 ("add new group");
		 $(".add-new-group").removeAttr("disabled");
		    var empty = false;
			var input = $(this).parents("tr").find('input[type="text"]');
			input.each(function(){
				if(!$(this).val()){
					$(this).addClass("error");
					empty = true;
				} else{
		         $(this).removeClass("error");
		     }
			});
			$(this).parents("tr").find(".error").first().focus();
			if(!empty){
				$(this).parents("tr").find(".addNewUser, .cancelAddNewUser").hide();
				$(".add-new-group").removeAttr("disabled");
			 
				$.ajax({
					type : "POST",
					contentType : "application/json",
					url : "/group/addgroup",
					data : saparator+$('#gname').val()+saparator+$('#gprefix').val(),
					dataType : 'json',
					success : function(result) {
						if(result.status == "Done"){
							getRoles();
		 				}else if(result.status == "Session expired"){
							location.reload();
						}
						console.log(result);
					},
					error : function(e) {
						alert("Error!"+ e)
					}
				});
			}
		});
	
	$(document).on("click", ".removerole", function(){ 
		event.preventDefault();
		("delete role");
		var removeGroupId = $(this).parent().siblings(":first").text();
		("removeGroupId"+removeGroupId);
		$(".add-new-group").removeAttr("disabled");
			  $.ajax({
					type : "DELETE",
					contentType : "application/json",
					url : "/group/deletegroup",
					data : removeGroupId,
					dataType : 'json',
					
					success : function(result) {
						if(result.status == "Done"){
							$(this).parents("tr").remove();
							getRoles();
		 				}else if(result.status == "Session expired"){
							location.reload();
						}else{
		 				}
						console.log(result);
					},
					error : function(e) {
						alert("Error!"+ e)
					}
				})
		});
	
	$(document).on("click", ".addNewUser", function(){
		 //alert("add new script"+ selectedScriptId + $(".saveScriptInput").attr('id'));
		 $(".add-new-user").removeAttr("disabled");
		    var empty = false;
			var input = $(this).parents("tr").find('input[type="text"]', 'option:selected');
			input.each(function(){
				if(!$(this).val()){
					$(this).addClass("error");
					empty = true;
					//alert("empty")
				} else{
		         $(this).removeClass("error");
		     }
			});
			$(this).parents("tr").find(".error").first().focus();
			if(!empty){
				var currentRow=$(this).closest("tr"); 
		        var col2=$('#name').val(); // get current row 2nd TD
		        var col3=$('.role').find(":selected").val();  // get current row 3rd TD
		        var col4=$('#enabled').find(":selected").val(); // get current row 4th TD 
		        //alert("true? :: "+ col4);
		        var postContent = saparator +col2+saparator +col3+saparator +col4; 
				$(".add-new-user").removeAttr("disabled");
				$.ajax({
					type : "POST",
					contentType : "application/json",
					url : "/user/adduser",
					data : postContent,
					dataType : 'json',
					success : function(result) {
						if(result.status == "Done"){
							$(this).parents("tr").find(".addNewUser, .cancelAddNewUser").hide();
							getUsers(true);
		 				}else if(result.status == "Session expired"){
							location.reload();
						}else if(result.status == "duplicate"){
							$('#name').focus();
							alert("username is already exist in db!!");
						console.log(result);
					}
						console.log(result);
					},
					error : function(e) {
						alert("Error!"+ e)
					}
				});
			}
		});
	
	//add new user
	$(document).on("click", ".add-new-user", function(){
			//alert("add new user >>");
	    	$(this).attr("disabled", "disabled");
			var index = $("table-inputs tbody tr:last-child").index();
			$("#userTbl").show();
			var row = '<tr>' +
	            '<td class="filterable-cell"></td>' +
	            '<td class="filterable-cell"><input type="text" class="form-control" name="name" id="name"></td>' +
	            '<td class="filterable-cell">'+addRolesForAddNewUserDropdown()+'</td>'+
	            '<td class="filterable-cell"><select id="enabled"> <option value="1" >true</option><option value="0" >false</option></select></td>'+
	            '<td class="filterable-cell"><a href="#" class="addNewUser"> Save </a><a href="#" class="cancelAddNewUser"> Cancel </a></td>' +
	            '</tr>';
			$("#userTbl").append(row);
			
	    	$(row).insertBefore('table table-inputs > tbody > tr:first');
			$("table table-inputs tbody tr").eq(index + 1).find(".add, .edit").toggle();
	});
	
	function addRolesForAddNewUserDropdown(){
		var content ='<select class="role">';
		$.each(roleList, function(i){	
				content = content + '<option value="'+roleList[i].roleId+'" >'+roleList[i].roleName+'</option>';
		});
		content = content+ '</select>';
		return content ;
	}
	
	var selectedIndex;
	function addRolesInDropDown(selected){
		var content ='<select class="updaterole">';
		$.each(roleList, function(i){	
			if(roleList[i].roleName == selected){
				//alert("yes selected" + roleList[i].appRole.roleId )
				selectedIndex = roleList[i].roleId;
				content = content + '<option value="'+roleList[i].roleId+'" selected="selected" >'+roleList[i].roleName+'</option>';	
			}else{
				content = content + '<option value="'+roleList[i].roleId+'" >'+roleList[i].roleName+'</option>';
			}
		});
		content = content+ '</select>';
		return content ;
	}
	
	function addRolesWithPrefixInDropDown(selected){
		//alert(selected)
		var content ='<select class="updaterole">';
		$.each(roleList, function(i){	
			//alert(roleList[i].rolePrefix +","+ selected)
			if(roleList[i].rolePrefix == selected){
				("yes selected" + roleList[i].rolePrefix )
				//selectedIndex = roleList[i].roleId;
				content = content + '<option value="'+roleList[i].rolePrefix+'" selected="selected" >'+roleList[i].roleName+'</option>';	
			}else{
				content = content + '<option value="'+roleList[i].rolePrefix+'" >'+roleList[i].roleName+'</option>';
			}
		});
		content = content+ '</select>';
		(content)
		return content ;
	}
	
	var selectedRole;
	$(document).on("change", "#selectGroup", function(){
		selectedRole = $(this).children("option:selected").val(); 
	        //alert("You have selected the role - " + selectedRole);
	        $("#userTbl td").remove();
			$("#userTbl").show();
			$.ajax({
				type : "GET",
				contentType : "application/json",
				url : "/user/getusers" ,
				//data : scriptid=,
				dataType : 'json',
				success : function(result) {
					if(result.status == "Done"){ 
						usrAndRoles = result; 
				        //alert("You have selected the role - " + selectedRole);
				        $("#userTbl td").remove();
						$("#userTbl").show();
				    	$.each(result.data, function(i){
				    		if(result.data[i].appRole.roleId == selectedRole){
				    			$("#userTbl").append('<tr><td>'+result.data[i].appUser.userId+'</td><td>'+result.data[i].appUser.userName+'</td><td>'+result.data[i].appRole.roleName+'</td><td>'+result.data[i].appUser.enabled+'</td>'
				    					+'<td><a href="#" class="edituser"> Edit </a> <a href="#"  class="removeuser" >Remove</a>'
				    			+'<a href="#" class="saveuser" style="display: none;" > Save </a><a href="#" class="canceluser" style="display: none;"> Cancel </a></td></tr>');
				    		}// 
				    	});	
					  			 
	 				}else if(result.status == "Session expired"){
						location.reload();
					} 
					console.log(result);
				},
				error : function(e) {
					alert("Error!"+ e)
				}
			});	
	});	
	
	$(document).on("click", ".editrole", function(){ 
		//alert("You have clicked the edit user link");
		event.preventDefault();
		currentValue = "";
		$(this).parents("tr").find('td').each(
			function (i) {
				     if(i!=3){
				    	 currentValue = currentValue + saparator + $(this).text();
				     }
				      
				    if(i != 0 && i != 3){
				          $(this).html('<input type="text" id ='+ i +' class="form-control" value="' + $(this).text() + '">');      
					}
				   				    
	    }); 
		//alert("before change :"+ currentValue);	
		$(this).parents("tr").find(".editrole, .removerole").toggle();
		$(this).parents("tr").find(".saverole, .cancelrole").toggle();
	});
	
	 var selectedDropdownRole;
	$(document).on("click", ".edituser", function(){ 
		//alert("You have clicked the edit user link");
		event.preventDefault();
		currentValue = "";
		$(this).parents("tr").find('td').each(
			function (i) {
				     if(i!=4){
				    	 currentValue = currentValue + saparator + $(this).text();
				     }
				     if(i == 2){
				    	 currentValue = currentValue + saparator + $(this).text();
				    	 selectedDropdownRole = $(this).html();
					    	$(this).html(addRolesInDropDown($(this).html()));
					    	var map = {};
							$('.updaterole option').each(function () {
							    if (map[this.value]) {
							        $(this).remove()
							    }
							    map[this.value] = true;
							})	
							
							$(".updaterole").val(selectedIndex);
							
				     }
				    if(i != 0 && i != 2 && i!=4){
				          $(this).html('<input type="text" id ='+ i +' class="form-control" value="' + $(this).text() + '">');      
					}	   				    
	    }); 
		//alert("before change :"+ currentValue);	
		$(this).parents("tr").find(".edituser, .removeuser").toggle();
		$(this).parents("tr").find(".saveuser, .canceluser").toggle();
	});
	
	$(document).on("click", ".canceluser", function(){ 
    	//alert("You have clicked the cancel");
    	event.preventDefault();
    	$(this).parents("tr").find(".edituser, .removeuser").show();
    	var input = $(this).parents("tr").find('input[type="text"]');
    	input.each(function(){    	
    		$(this).parent("td").html($(this).val())
    	});
    	
    	$(this).parents("tr").find('td').each(
    			function (i) {
    				if(i==2){
    					$(this).html(selectedDropdownRole);
    				}
    	});
    	
    	$(this).parents("tr").find(".saveuser").hide();
    	$(this).parents("tr").find(".canceluser").hide();
	});
	
	$(document).on("click", ".removeuser", function(){ 
		event.preventDefault();
		("delete user");
		var removeUserId = $(this).parent().siblings(":first").text();	 
		$(".add-new-user").removeAttr("disabled");
			  $.ajax({
					type : "POST",
					contentType : "application/json",
					url : "/user/deleteuser",
					data : removeUserId,
					dataType : 'json',
					
					success : function(result) {
						if(result.status == "Done"){
							$(this).parents("tr").remove();
							getUsers(true);
							//showUsers();
		 				}else if(result.status == "Session expired"){
							location.reload();
						}else{
		 				}
						console.log(result);
					},
					error : function(e) {
						alert("Error!"+ e)
					}
				})
		});
	
  $(".add-new").click(function(){
		//alert("add new script");
    	$(this).attr("disabled", "disabled");
		var index = $("table tbody tr:last-child").index();
        var row = '<tr>' +
            '<td id= "newScriptId"></td>' +
            '<td><input type="text" class="form-control" name="sname" id="sname"></td>' +
            '<td><input type="text" class="form-control" name="desc" id="desc"></td>' +
            '<td><input type="text" class="form-control" name="location" id="location"></td>' +
            '<td><input type="text" class="form-control" name="prefix" id="prefix"></td>' +
            '<td>'+addRolesWithPrefixInDropDown("")+'</td>' +
    		'<td class="filterable-cell" ><a href="#" class="runnewscript" style="display: none;" >run</a></td>'+
    	    '<td class="filterable-cell"> <a href="#" class="editnewscript" style="display: none;"> Edit </a> <a href="#"  class="removenewscript" style="display: none;" >Remove</a>'+ 
    		'<a href="#" class="addnewscript"> Save </a><a href="#" class="updatescript" style="display: none;"> Save </a><a href="#" class="cancelnewscript"> Cancel </a></td>' +
        '</tr>';
        $("#manageScripts td").remove();
        $("#manageScripts").append(row);
        $("#manageScripts").show();
    	//$(row).insertBefore('table > tbody > tr:first'); 
		$("table tbody tr").eq(index + 1).find(".add, .edit").toggle(); 
    });
    
    $(document).on("change", ".roleaccess", function(){
        var selectedRole = $(this).children("option:selected").val();
        //alert("You have selected the role - " + selectedRole);
    });
    
    $(document).on("click", ".cancelAddNewUser", function(){
    	 //alert("cancel new script");
    	 $(this).parents("tr").remove();
    	 $(".add-new-user").removeAttr("disabled");
    	});

    
    //cancel to add new script
    $(document).on("click", ".canceladdnewscript", function(){
     //alert("cancel new script");
     $(this).parents("tr").remove();
	 $(".add-new").removeAttr("disabled");
    });
    var newScriptNameWithPrefix;     
    //save button clicks..
	$(document).on("click", ".addnewscript", function(){
		//alert("save new script");
		var newscriptcontent="";
		var empty = false;
		var input = $(this).parents("tr").find('input[type="text"]', 'option:selected');
	    input.each(function(){
			if(!$(this).val()){
				$(this).addClass("error");
				empty = true;
			} else{
	            $(this).removeClass("error");
	        }
		});
	    
		$(this).parents("tr").find(".error").first().focus();
		var scriptName;
		var loc;
		var prefix;
	 
		if(!empty){
			var selectedR = $('.updaterole').find(":selected").val();
			$(this).parents("tr").find('td').each (function(i) {
				if(i==5){
					//alert("adding value")
					$(this).html(selectedR);
				}
			});		
			input.each(function(i){
				$(this).parent("td").html($(this).val());
				newscriptcontent = newscriptcontent + saparator + $(this).val() ;
						if(i== 0){
	    			    	 scriptName = $(this).val();
	    			     }
	    			     if(i== 2){
	    			    	 loc=$(this).val(); 
	    			     }
	    			     if(i== 3){
	    			    	 prefix=$(this).val();
	    			     }
			});
			
			newscriptcontent = newscriptcontent + saparator + selectedR;
			newScriptNameWithPrefix=loc+scriptName+"."+prefix;
			 //alert(newscriptcontent)
			$(this).parents("tr").find(".addnewscript, .cancelnewscript").hide();
			$(this).parents("tr").find(".editnewscript, .removenewscript").show();
			//$(this).parents("tr").find(".cancelnewescript").show();
			$(this).parents("tr").find(".runnewscript").show();
			$(".add-new").removeAttr("disabled");
			var tmpScrId;
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "/api/addnew",
				data : newscriptcontent,
				dataType : 'json',
				success : function(result) {
					if(result.status == "Done"){
						//alert(result.data.scriptId);
						$(this).parents("tr").find(".updatescript").attr('id', result.data.scriptId);
						input.each(function(){
							$("#newScriptId").html(result.data.scriptId);
							getRoles();
							getScripts(scriptName);
						});	
						tmpScrId =  result.data.scriptId;
						 
	 				}else if(result.status == "Session expired"){
						location.reload();
					}else{
	 				}
					console.log(result);
				},
				error : function(e) {
					alert("Error!"+ e)
				}
			});
		}	
    });
		
//remove new
$(document).on("click", ".removenewscript", function(){
		event.preventDefault(); 
		//alert($(this).parent().siblings(":first").text());
		var removeScriptId = $(this).parent().siblings(":first").text();
		 $(this).parents("tr").remove();
		 $(".add-new").removeAttr("disabled");
		 $.ajax({
				type : "POST",
				contentType : "application/json",
				url : "/api/delete",
				data : removeScriptId,
				dataType : 'json',
				
				success : function(result) {
					if(result.status == "Done"){
						// $("#main").slideUp("slow");
						// $("#main").hide(1000);
					}else if(result.status == "Session expired"){
						location.reload();
					}
					console.log(result);
				},
				error : function(e) {
					alert("Error!"+ e)
				}
			});	 	
	});
//remove link
$(document).on("click", ".removescript", function(){
		event.preventDefault();
		var removeScriptId = $(this).attr('id');	 
		 $(this).parents("tr").remove();
		 $(".add-new").removeAttr("disabled");
		 $.ajax({
				type : "DELETE",
				contentType : "application/json",
				url : "/api/delete",
				data : removeScriptId,
				dataType : 'json',
				
				success : function(result) {
					if(result.status == "Done"){
						// $("#main").slideUp("slow");
						// $("#main").hide(1000);
					}else if(result.status == "Session expired"){
						location.reload();
					}
					console.log(result);
				},
				error : function(e) {
					alert("Error!"+ e)
				}
			});	 	
	});	

	//admin page edit link	
	$(document).on("click", ".editnewscript", function(){ 
	//event.preventDefault();
	//disable run link
	//alert($(this).parent().siblings(":first").text())
	$('.runnewscript').hide();
    var id = ""; 
	$(this).parents("tr").find('td').each(
		function (i) {
			//alert($(this).text())
			     if(i != 6 && i != 7){
			    	 currentValue = currentValue + saparator + $(this).text();
			    	 if(i != 0)
			          $(this).html('<input type="text" class="form-control" value="' + $(this).text() + '">');
			         //currentValue = currentValue + ","+ $(this).text();
				}
    });
	$(this).parents("tr").find(".editnewscript, .removenewscript").hide();
	$(this).parents("tr").find(".updatescript").show();
	$(this).parents("tr").find(".cancelnewscript").show();
	$(this).parents("tr").find(".runnewscript").hide();
	//set script id
	$(this).parents("tr").find(".updatescript").attr('id', $(this).parent().siblings(":first").text());
	//$(this).parents("tr").find(".removenewscript").attr('id', $(this).parent().siblings(":first").text());
	$(this).parents("tr").find(".addnewscript").attr('id', $(this).parent().siblings(":first").text());
	$(this).parents("tr").find(".cancelnewscript").attr('id', $(this).parent().siblings(":first").text());
	 
});	

$(document).on("click", ".cancelnewscript", function(){
	//alert("222");
	var input = $(this).parents("tr").find('input[type="text"]');
    input.each(function(){
		if(!$(this).val()){
			empty = true;
		}  
	}); 
    if(empty){
    	$(this).parents("tr").remove();
    	$(".add-new").removeAttr("disabled");
    }else{
	 input.each(function(){
		$(this).parent("td").html($(this).val());
		updatedvalue = updatedvalue + saparator + $(this).val() ;;
	});
	 $('.run').show();
	 $('.runnewscript').show();
	$(this).parents("tr").find(".editnewscript, .removenewscript").show();
	$(this).parents("tr").find(".updatescript").hide();
	$(this).parents("tr").find(".cancelnewscript").hide();
	$(this).parents("tr").find(".runnewscript").show();
    }
});
$(document).on("click", ".editscript", function(){
//$(".editscript").click(function(event) {
	//alert("edit");
	event.preventDefault();
	//disable run link
	$('.run').hide();
	currentValue = saparator + $(this).attr('id');
	$(this).parents("tr").find('td').each(
		function (i) {
			//alert($(this).text())
			     if(i != 6 && i != 7){
			    	 currentValue = currentValue + saparator + $(this).text();
			         //currentValue = currentValue + ","+ $(this).text();
			    	 if(i == 5){
				    	 //currentValue = currentValue + saparator + $(this).text();
				    	 selectedDropdownRole = $(this).text();
					    	$(this).html(addRolesWithPrefixInDropDown($(this).text()));
							$(".updaterole").val(selectedDropdownRole);
				     }
			    	 if(i != 0 && i != 5)
				          $(this).html('<input type="text" class="form-control" value="' + $(this).text() + '">');
			     }
    });
	//alert("before change :"+ currentValue)
	$(this).parents("tr").find(".editscript, .removescript").toggle();
	$(this).parents("tr").find(".updatescript").show();
	$(this).parents("tr").find(".cancelupdatescript").show();
});	


$(document).on("click", ".cancelupdatescript", function(){
	var input = $(this).parents("tr").find('input[type="text"]');
	input.each(function(){
		$(this).parent("td").html($(this).val());
		updatedvalue = updatedvalue + saparator + $(this).val() ;;
	});
	$(this).parents("tr").find('td').each(
			function (i) {
				if(i==5){
					$(this).html(selectedDropdownRole);
				}
	});
	$(this).parents("tr").find(".editscript, .removescript").toggle();
	//$(".updatescript").hide();
	$(this).parents("tr").find(".updatescript").hide();
	$(this).parents("tr").find(".cancelupdatescript").hide();
	$('.run').show();
	
});

//Add row on add button click
$(document).on("click", ".updatescript", function(){
	var empty = false;
	var selectedScriptId=$(this).attr('id');
	//currentValue = saparator + $(this).attr('id');
	var input = $(this).parents("tr").find('input[type="text"]');
    input.each(function(){
		if(!$(this).val()){
			$(this).addClass("error");
			empty = true;
		} else{
            $(this).removeClass("error");
        }
	});
	$(this).parents("tr").find(".error").first().focus();
	
	if(!empty){
		updatedvalue = saparator + selectedScriptId;
		input.each(function(){
			$(this).parent("td").html($(this).val());
			updatedvalue = updatedvalue + saparator + $(this).val() ;
		});	
		
		updatedvalue = updatedvalue + saparator +  $('.updaterole').find(":selected").val(); 
		//alert("after changing:"+ updatedvalue);
		
		if(updatedvalue == currentValue)
			{
			//alert("both are same");
			}else {
				//alert("both are different .. update it in db");
				//alert(">>"+updatedvalue);
		    	$.ajax({
					type : "PUT",
					contentType : "application/json",
					url : "/api/save",
					data : updatedvalue,
					dataType : 'json',
					success : function(result) {
						if(result.status == "Done"){
							getRoles();
							getScripts(searchScriptParam);
		 				}else if(result.status == "Session expired"){
							location.reload();
						}
						console.log(result);
					},
					error : function(e) {
						alert("Error!"+ e)
					}
				});
			}
		updatedvalue = "";
		currentValue = "";
		$(this).parents("tr").find(".editscript, .removescript").toggle();
		//$(".updatescript").hide();
		$(this).parents("tr").find(".updatescript").hide();
		$(this).parents("tr").find(".cancelupdatescript").hide();
		
		
		$('.run').show();
		
		$(this).parents("tr").find(".editnewscript, .removenewscript").show();
		$(this).parents("tr").find(".cancelnewscript").hide();
		$(this).parents("tr").find(".runnewscript").show();
		
	}		
});
$(".updatescript").hide();
$(".cancelupdatescript").hide();

// SUBMIT FORM
$("#inputForm").submit(function(event) {
		// Prevent the form from submitting via the browser.
    	event.preventDefault();
    	var inputs = " ";
    	$("form#inputForm :input").each(function(){
    		 var input = $(this); // This is the jquery object of the input
    		 trid = trid + " "+ input.val();
    		});
		//hide scriptlist
		$("#scriptlistDiv").hide(1000);
		ajaxPost();
	});

$(document).on("click", ".runnewscript", function(){
  	event.preventDefault();
	//get id of selected script name 
	trid = newScriptNameWithPrefix
	//get script name to display on top of script result div
	$(this).closest('tr').find('td').each(
			    function (i) {
			      if(i==1){
			    	  scriptName = $(this).text();
			      }
			    });
	$.ajax({
		type : "GET",
		url : "api/findinputs?name="+$(this).parent().siblings(":first").text(),
		dataType : 'json',
		success: function(result){
				if(result.status == "Done"){
					var inputList = "";
					$.each(result.data, function(i, scriptInputs){
						inputList = inputList + addInputboxes(scriptInputs.inputName, scriptInputs.inputType, scriptInputs.required);
					    console.log("Success: ", result);
					});
					//check if inputs retrived from db?
					if(inputList.length == 0){	
						$("#scriptlistDiv").hide(1000);
						ajaxPost();
					}else{
					$("#scriptlistDiv").hide();
					$("#postResultDiv1").html("<h4 style='font-family:calibri;'>"+ scriptName + " - Inputs</h4>"+inputList + "<div class=\"row\">\n" + 
							"<div class='col-12 col-sm-6 col-md-6'>\n" + 
							"<button type=\"reset\" class=\"btn btn-primary \">Reset</button>\n" + 
							"<button type=\"submit\" class=\"btn btn-primary \">Submit</button>\n" + 
							"</div>\n" + 
							"</div>");
					$("#postResultDiv1").prepend("<button type='button' id='close' class='close'><p style='font-size: 14px'>[ CLOSE ]</p></button>");+
					$("#main").show();
					$("#buttonDiv").show();
				}
			}else if(result.status == "Session expired"){
				location.reload();
			}else{
				$("#postResultDiv1").html("<strong>Error</strong>");
//				
				console.log("Fail: ", result);
			}
		},
		error : function(e) {
			$("#getResultDiv").html("<strong>Error</strong>");
			console.log("ERROR: ", e);
		}
	});	
	$("#main").show();
	$("#postResultDiv").hide();

});
$(document).on("click", ".run", function(){
//$(".run").click(function(event) {
    	event.preventDefault();
    	//get id of selected script name 
    	trid = $(this).closest('tr').attr('id'); 
    	//get script name to display on top of script result div
    	$(this).closest('tr').find('td').each(
    			    function (i) {
    			      if(i==1){
    			    	  scriptName = $(this).text();
    			      }
    			    });
    	//alert(trid);
    	$.ajax({
			type : "GET",
			url : "api/findinputs?name="+ $(this).attr("id"),
			//data : JSON.stringify($(this).attr("id")),
			dataType : 'json',
			success: function(result){
					if(result.status == "Done"){
						var inputList = "";
						$.each(result.data, function(i, scriptInputs){
							inputList = inputList + addInputboxes(scriptInputs.inputName, scriptInputs.inputType, scriptInputs.required);
						    console.log("Success: ", result);
						});
						//check if inputs retrived from db?
						if(inputList.length == 0){	
							$("#scriptlistDiv").hide(1000);
							ajaxPost();
						}else{
							$("#postResultDiv1").show();
						$("#scriptlistDiv").hide();
						$("#postResultDiv1").html("<h4 style='font-family:calibri;'>"+ scriptName + " - Inputs</h4>"+inputList + "<div class=\"row\">\n" + 
								"<div class='col-12 col-sm-6 col-md-6'>\n" + 
								"<button type=\"reset\" class=\"btn btn-primary \">Reset</button>\n" + 
								"<button type=\"submit\" class=\"btn btn-primary \">Submit</button>\n" + 
								"</div>\n" + 
								"</div>");
						$("#postResultDiv1").prepend("<button type='button' id='close' class='close'><p style='font-size: 14px'>[ CLOSE ]</p></button>");+
						$("#main").show();
						$("#buttonDiv").show();
					}
				}else if(result.status == "Session expired"){
					location.reload();
				}else{
					$("#postResultDiv1").html("<strong>Error</strong>");
//					
					console.log("Fail: ", result);
				}
			},
			error : function(e) {
				$("#getResultDiv").html("<strong>Error</strong>");
				console.log("ERROR: ", e);
			}
		});	
    	$("#main").show();
		$("#postResultDiv").hide();
	});
    	
    function addInputboxes(param, type, required){
    		return "<div class=\"form-group\">\r\n" + 
    				"<div class=\"col-sm-9\">\r\n" + 
    				" <input type=\""+ type +"\" th:field=\"*{"
    				+ param
    				+ "}\" placeholder=\""+ "Enter "+param+ "\" class=\"form-control\" "+ required+ "/>\r\n" + 
    				"</div>\r\n" + 
    				"</div>";
    }
    
    
    
    $(document).on('click','#close',function(){
	    // code here
		$("#scriptlistDiv").show();
		$("#postResultDiv").hide();
		$("#main").hide();
		
	});
    
//Call server for shell script
function ajaxPost(){
    	//alert(trid);
		$("#main").hide(1000);
		$("#postResultDiv").show();
		$("#postResultDiv").html("<h4>"+ scriptName + "</h4>" +
				"<div >Script is running please wait...</div>");
    	
    	// call shell script
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/api/execute",
			data : trid,
			dataType : 'json',
			
			success : function(result) {
				if(result.status == "Done"){
					//$("#main").slideUp("slow");
					//$("#main").hide(1000);
					$("#postResultDiv").show();
					$("#postResultDiv").html("<h4>"+ scriptName + "</h4>" + 
					        "<div style='height: 400px; width: 93vw; overflow: auto;'>" +
							"<button type='button' id='close' class='close'><p style='font-size: 14px'>[ CLOSE ]</p></button>"+
							"<p style='background-color:#FFF; color:block; padding:20px 20px 20px 20px' >" + 
							"Script has been executed. Please find the logs below<br><br>"+result.data + " <br>" +
							 "</p></div>");
				}else if(result.status == "Session expired"){
					location.reload();
				}else{ 
					$("#postResultDiv").html("<strong>Error in response. Please contact admin team</strong>");
				}
				console.log(result);
			},
			error : function(e) {
				alert("Error!"+ e)
				console.log("ERROR: ", e);
				$("#main").hide(1000);
				$("#postResultDiv").show();
				$("#postResultDiv").html("<strong>"+e+"</strong>");
			}
		});
    }


     
})