$( document ).ready(function() {
	
	$("#main").hide();
	//$("#scriptlistDiv").show(500);
	var trid;
	var scriptName;
	
     //admin page
	var currentValue ="";
	var updatedvalue = "";
	var saparator = "::@@::";
	 
	$("#manageScript").click(function(){
	    //alert("clicked");
	    event.preventDefault();
	    
	    $('#table td:nth-child(2)').hide();
	    
	    //alert($(this).closest('table').attr('id'));
	    
		//var column = "table ." + $(this).attr("Actions");
	    //$(column).toggle();
	});
	//add new script input
	$(document).on("click", ".add-new-input", function(){
		//alert("add new script");
    	$(this).attr("disabled", "disabled");
		var index = $("table table-inputs tbody tr:last-child").index();
		//alert(index);
		var row = '<tr>' +
            '<td><input type="text" class="form-control" name="inputName" id="inputName"></td>' +
            '<td><input type="text" class="form-control" name="inputType" id="inputType"></td>' +
            '<td><input type="text" class="form-control" name="required" id="required"></td>'+
    		'<td><a href="#" class="addNewScriptInput"> Save </a><a href="#" class="cancelAddNewScriptInput"> Cancel </a></td>' +
            '</tr>';
		$("table").append(row);	
    	$(row).insertBefore('table table-inputs > tbody > tr:first'); 
		$("table table-inputs tbody tr").eq(index + 1).find(".add, .edit").toggle();
    });
	//add new
	var actions = $("table td:last-child").html();
	// Append table with add row form on add new button click
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
            '<td><input type="text" class="form-control" name="access" id="access"></td>' +
    		'<td class="filterable-cell" ><a href="#" class="runnewscript" style="display: none;" >run</a></td>'+
    	    '<td class="filterable-cell"> <a href="#" class="editnewscript" style="display: none;"> Edit </a> <a href="#"  class="removenewscript" style="display: none;" >Remove</a>'+ 
    		'<a href="#" class="addnewscript"> Save </a><a href="#" class="updatescript" style="display: none;"> Save </a><a href="#" class="cancelnewscript"> Cancel </a></td>' +
        '</tr>';	
    	$(row).insertBefore('table > tbody > tr:first'); 
		$("table tbody tr").eq(index + 1).find(".add, .edit").toggle(); 
    });
    
    $(document).on("change", ".roleaccess", function(){
        var selectedRole = $(this).children("option:selected").val();
        //alert("You have selected the role - " + selectedRole);
    });
    
    //cancel to add new script input
    $(document).on("click", ".cancelAddNewScriptInput", function(){
     //alert("cancel new script");
     $(this).parents("tr").remove();
	 $(".add-new-input").removeAttr("disabled");
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
			input.each(function(i){
				$(this).parent("td").html($(this).val());
				newscriptcontent = newscriptcontent + saparator + $(this).val() ;
	    			//alert($(this).text())
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
			
			newScriptNameWithPrefix=loc+scriptName+"."+prefix;
			 //alert(newScriptNameWithPrefix)
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
	
	//SCRIPT INPUT Starts
	
	$(document).on("click", ".removeScriptInput", function(){ 
		console.log("You have clicked the removeScriptInput");
			alert("You have clicked the removeScriptInput" );	
		});
	$(document).on("click", ".cancelScriptInput", function(){ 
	    	alert("You have clicked the cancelScriptInput");
	    	event.preventDefault();
		});
	// save link on edit scexisting script input row
    $(document).on("click", ".saveScriptInput", function(){ 
    	alert("You have clicked the saveScriptInput"+ $(this).attr('id'));
    	event.preventDefault();
    	var empty = false;
    	var selectedScriptId=$(this).attr('id');
    	//alert("sssfd>>>> "+selectedScriptId);
    	var input = $(this).parents("tr").find('input[type="text"]');
        input.each(function(){
    		//alert($(this).val())
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
    		
    		//alert("after changing:"+ updatedvalue);
    	}
    	
    	if(updatedvalue == currentValue)
		{
		 //alert("both are same");
		}else {
		  //alert("both are different .. update it in db");
		  //
		  $.ajax({
				type : "POST",
				contentType : "application/json",
				url : "/api/updateInputs",
				data : updatedvalue,
				dataType : 'json',
				
				success : function(result) {
					if(result.status == "Done"){
						 
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
		}		
	});
    $(document).on("click", ".editScriptInput", function(){ 
    	//alert("You have clicked the editScriptInput");
    	event.preventDefault();
    	//disable run link
    	//$('.run').hide();
    	currentValue = saparator + $(this).attr('id');
    	$(this).parents("tr").find('td').each(
    		function (i) {
    			//alert($(this).text())
    			     if(i!= 3){
    			    	 //alert("current test"+$(this).text())
    			    	 currentValue = currentValue + saparator + $(this).text();
    			    	// if(i != 0)
    			          $(this).html('<input type="text" class="form-control" value="' + $(this).text() + '">');
    			         //currentValue = currentValue + ","+ $(this).text();
    				}
        });
    	//alert("before change :"+ currentValue);	
    	$(this).parents("tr").find(".editScriptInput, .removeScriptInput").toggle();
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
$(".removescript").click(function(event) {
		event.preventDefault();
		var removeScriptId = $(this).attr('id');	 
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
	//set script id
	$(this).parents("tr").find(".updatescript").attr('id', $(this).parent().siblings(":first").text());
	//$(this).parents("tr").find(".removenewscript").attr('id', $(this).parent().siblings(":first").text());
	$(this).parents("tr").find(".addnewscript").attr('id', $(this).parent().siblings(":first").text());
	$(this).parents("tr").find(".cancelnewscript").attr('id', $(this).parent().siblings(":first").text());
	 
});	

$(document).on("click", ".cancelnewscript", function(){
	var input = $(this).parents("tr").find('input[type="text"]');
	input.each(function(){
		$(this).parent("td").html($(this).val());
		updatedvalue = updatedvalue + saparator + $(this).val() ;;
	});
	 $('.run').show();
	$(this).parents("tr").find(".editnewscript, .removenewscript").show();
	$(this).parents("tr").find(".updatescript").hide();
	$(this).parents("tr").find(".cancelnewscript").hide();
	$(this).parents("tr").find(".runnewscript").show();
	
});

$(".editscript").click(function(event) {
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
			    	 if(i != 0)
			          $(this).html('<input type="text" class="form-control" value="' + $(this).text() + '">');
			         //currentValue = currentValue + ","+ $(this).text();
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
		
		//alert("after changing:"+ updatedvalue);
		
		if(updatedvalue == currentValue)
			{
			//alert("both are same");
			}else {
				//alert("both are different .. update it in db");
				//alert(">>"+updatedvalue);
		    	$.ajax({
					type : "POST",
					contentType : "application/json",
					url : "/api/save",
					data : updatedvalue,
					dataType : 'json',
					success : function(result) {
						if(result.status == "Done"){
							//$("#main").slideUp("slow");
							//$("#main").hide(1000);
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
//hide script list by default    
//$('#scriptlistDiv').hide();
$(".manageScripts").click(function(event) {
	$('#scriptlistDiv').show();
});

$(".manageScripts").click(function(event) {
	$('#addRemoveInputsDiv').hide();
	$('#addRemoveUsersDiv').hide();
	$('#scriptlistDiv').show();
	$('#addRemoveGroupsDiv').hide();
});

$(".manageUsers").click(function(event) {
	$('#addRemoveInputsDiv').hide();
	$('#addRemoveUsersDiv').show();
	$('#scriptlistDiv').hide();
	$('#addRemoveGroupsDiv').hide();
});

$(".manageScriptInputs").click(function(event) {
	$('#addRemoveUsersDiv').hide();
	//$('#addRemoveInputsDiv').show();
	$('#scriptlistDiv').hide();
	$('#addRemoveGroupsDiv').hide(); 
});

$(".manageGroup").click(function(event) {
	$('#addRemoveGroupsDiv').show();
	$('#addRemoveUsersDiv').hide();
	$('#addRemoveInputsDiv').hide();
	$('#scriptlistDiv').hide();	 
});

//Manage Input drop down selected..
$(document).on("change", "#selectScriptName", function(){
    var selectedScriptId = $(this).children("option:selected").val();
    //alert("You have selected the script name - " + selectedRole);
    var  noInputsInDb = false;
    $.ajax({
		type : "GET",
		url : "api/all?name="+ selectedScriptId , 
		dataType : 'json',
		success: function(result){
				if(result.status == "Done"){
					var inputList = "";
					$.each(result.data, function(i, scriptInputs){
					    inputList = inputList + showInputs(scriptInputs.scriptId, scriptInputs.inputName, scriptInputs.inputType, scriptInputs.required);
						//alert(inputList);
						console.log("Success: ", result);
					});
					//check if inputs retrived from db?
					if(inputList.length == 0){	
						noInputsInDb = true;
						//$("#scriptInputResultDiv").hide();
						$("#scriptInputResultDiv").html("<div class=\"col-sm-4\">\r\n" + 
					    		"			<button type=\"button\" class=\"btn btn-info add-new-input\">\r\n" + 
					    		"				<i class=\"fa fa-plus\"></i> Add New\r\n" + 
					    		"			</button>\r\n" + 
					    		"    	</div>" +
					    		"<table class=\"table table-inputs table-striped\" style= \" width:100% \" id=\"manageScriptInputsTbl\" >\r\n" + 
					    		"    <thead>\r\n" + 
					    		"      <tr>\r\n" + 
					    		"        <th>inputName</th>\r\n" + 
					    		"        <th>inputType</th>\r\n" + 
					    		"        <th>required</th>\r\n" + 
					    		"        <th>Action</th>\r\n" + 
					    		"      </tr>\r\n" + 
					    		"    </thead>\r\n" + 
					    		"    <tbody style=\"width:100%;\">\r\n" + 
					    		"    </tbody>\r\n" + 
					    		"  </table>\r\n" );
					}else{
						$("#scriptInputResultDiv").html("<div class=\"col-sm-4\">\r\n" + 
					    "			<button type=\"button\" class=\"btn btn-info add-new-input\">\r\n" + 
					    "				<i class=\"fa fa-plus\"></i> Add New\r\n" + 
					    "			</button>\r\n" + 
					    "    	</div>" +
					    "<table class=\"table table-inputs table-striped\" style= \" width:100% \" id=\"manageScriptInputsTbl\" >\r\n" + 
			    		"    <thead>\r\n" + 
			    		"      <tr>\r\n" + 
			    		"        <th>inputName</th>\r\n" + 
			    		"        <th>inputType</th>\r\n" + 
			    		"        <th>required</th>\r\n" + 
			    		"        <th>Action</th>\r\n" + 
			    		"      </tr>\r\n" + 
			    		"    </thead>\r\n" + 
			    		"    <tbody style=\"width:100%;\">\r\n" + 
			    		
			    		inputList + 
			    		 
			    		"    </tbody>\r\n" + 
			    		"  </table>\r\n" );
				}
			}else if(result.status == "Session expired"){
				location.reload();
			}else{
				$("#postResultDiv1").html("<strong>Error</strong>");		
				console.log("Fail: ", result);
			}
		},
		error : function(e) {
			$("#getResultDiv").html("<strong>Error</strong>");
			console.log("ERROR: ", e);
		}
	});	
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
		url : "api/all?name="+$(this).parent().siblings(":first").text(),
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
$(".run").click(function(event) {
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
			url : "api/all?name="+ $(this).attr("id"),
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
    
    function showInputs(id, param, type, required){
    	return "<tr>\r\n" +
		"    <td class=\"filterable-cell\" >"+param+"</td>\r\n" + 
		"    <td class=\"filterable-cell\" >"+type+"</td>\r\n" + 
		"    <td class=\"filterable-cell\" >"+required+"</td>\r\n"+
		"    <td class=\"filterable-cell\" > "+
		"    <a href='#' id="+ id +" class='editScriptInput'> Edit </a> " +
		"    <a href='#' id="+ id +" class='removeScriptInput'>Remove</a>"+ 
        "    <a href='#' id="+ id +" class='saveScriptInput'>Save </a>" +
        "    <a href='#' id="+ id +" class='cancelScriptInput'> Cancel </a>" +
        "    </td>"+
        "    <tr>\r\n" ;
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
					$("#postResultDiv").html("<strong>Error</strong>");
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