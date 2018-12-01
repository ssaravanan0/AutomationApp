$( document ).ready(function() {
	
	$("#main").hide();
	$("#scriptlistDiv").show(500);
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
	
	//add new
	var actions = $("table td:last-child").html();
	// Append table with add row form on add new button click
    $(".add-new").click(function(){
		//alert("add new script");
    	$(this).attr("disabled", "disabled");
		var index = $("table tbody tr:last-child").index();
        var row = '<tr>' +
            '<td></td>' +
            '<td><input type="text" class="form-control" name="name" id="name"></td>' +
            '<td><input type="text" class="form-control" name="desc" id="department"></td>' +
            '<td><input type="text" class="form-control" name="location" id="location"></td>' +
            '<td><input type="text" class="form-control" name="prefix" id="prefix"></td>' +
            '<td> <select  class="form-control roleaccess">\r\n' + 
    		'<option value=\"AU\">AU</option>\r\n' + 
    		'<option value=\"U\">U</option>\r\n' + 
    		'<option value=\"A\">A</option>\r\n' + 
    		'</select>'+
    		'<td> <a href="#" class="addnewscript"> Save </a><a href="#" class="canceladdnewscript"> Cancel </a></td>' +
        '</tr>';
        //<input type="text" class="form-control" name="access" id="access">*/</td>' +
    	//$("table").append(row);	
    	$(row).insertBefore('table > tbody > tr:first'); 
		$("table tbody tr").eq(index + 1).find(".add, .edit").toggle();
    });
    
    $(document).on("change", ".roleaccess", function(){
        var selectedRole = $(this).children("option:selected").val();
        //alert("You have selected the role - " + selectedRole);
    });
    
    //cancel to add new script
    $(document).on("click", ".canceladdnewscript", function(){
     //alert("cancel new script");
     $(this).parents("tr").remove();
	 $(".add-new").removeAttr("disabled");
    });
    //save button clicks..
	$(document).on("click", ".addnewscript", function(){
		//alert("save new script");
		var newscriptcontent="";
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
			input.each(function(){
				//$(this).parent("td").html($(this).val());
				newscriptcontent = newscriptcontent + saparator + $(this).val() ;
			});	
			newscriptcontent = newscriptcontent + saparator +  $(this).parents("tr").find('option:selected').val();
			//alert(input2)
			//alert(newscriptcontent);
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "/api/addnew",
				data : newscriptcontent,
				dataType : 'json',
				
				success : function(result) {
					if(result.status == "Done"){
						//$("#main").slideUp("slow");
						//$("#main").hide(1000);
						//$(this).parents("tr").find(".addnewscript, .cancelnewscript").toggle();
						//$(this).parents("tr").find(".updatescript").show();
						//$(this).parents("tr").find(".cancelupdatescript").show();
						location.reload(); 
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
		//alert(newscriptcontent)
	 		
    });
	
	//remove link
	$(".removescript").click(function(event) {
		event.preventDefault();
		var removeScriptId;
		$(this).parents("tr").find('td').each(
			function (i) {
				    if(i == 0){
				    	 removeScriptId = $(this).text();
					}
	         }); 
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
		 
		 	
	});	
	//admin page edit link
$(".editscript").click(function(event) {
	event.preventDefault();
	
	//disable run link
	$('.run').hide();

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
	var selectedScriptId=$(this).closest('tr').children('td:first').text();
	
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
			updatedvalue = updatedvalue + saparator + $(this).val() ;;
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
						}else{
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
$(".manageScripts").click(function(event) {
	
    alert("--");	
});

$(".manageScriptInputs").click(function(event) {
	
    alert("-----");	
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
    
    
    $(document).on('click','#close',function(){
	    // code here
		$("#scriptlistDiv").show();
		$("#postResultDiv").hide();
		$("#main").hide();
		
	});
    
    function ajaxPost(){
    	//alert(trid);
		$("#main").hide(1000);
		$("#postResultDiv").show();
		$("#postResultDiv").html("<h4>"+ scriptName + "</h4>" +
				"<div >Script is running please wait...</div>");
		
    	// PREPARE FORM DATA
    	var formData = {
    		trid
    	}
    	
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
    
    function resetData(){
    	
    }
    
 function ajaxSave(){
    	 
    	// PREPARE FORM DATA
    	var formData = {
    			updatedvalue
    	}
    	
    	// 
    	$.ajax({
			type : "POST",
			//contentType : "application/json",
			url : "/api/save",
			//url : "user",
			data : updatedvalue,
			//dataType : 'json',
			
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
})