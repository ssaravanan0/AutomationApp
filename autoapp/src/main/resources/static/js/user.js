$( document ).ready(function() {
	
	$("#main").hide();
	$("#scriptlistDiv").show(500);
	var trid;
	var scriptName;
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
    
})