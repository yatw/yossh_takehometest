 
function DisplayExpense(resultData) {

	console.log(resultData);
	console.log(resultData.length);
	
    let ExpenseTableElement = jQuery("#ExpenseTable");
    
    let rowHTML = "";
    let count = 0;
    for (count; count < resultData.length; count++) {
    	
    	
		if (count%2 == 0) {
			rowHTML+= "<tr class=\"normal\">";
		}else {
			rowHTML+= "<tr class=\"alt\">"; 
		}
		 
		
		//console.log(resultData[count]["movie_id"]);
		rowHTML+="<td>" + resultData[count]["id"] + "</td>";	
 		rowHTML+="<td>" + resultData[count]["e_date"] + "</td>";
 		rowHTML+="<td>" + resultData[count]["e_value"] + "</td>";
 		rowHTML+="<td>" + resultData[count]["e_reason"] + "</td>";
 		rowHTML+="</tr>";
	
    }
    
    // Append the row created to the table body, which will refresh the page
    ExpenseTableElement.append(rowHTML);
    
}

function ShowExpense(errorData){
	
	errorData = JSON.parse(errorData);
	// resultData is error message
	console.log(errorData);
	
	let valid = errorData["valid"];
	
	if (valid == "valid"){

		location.reload(); // clean up the table
	    jQuery.get(
	    	"api/displaytable",
	    	(resultData) => DisplayExpense(JSON.parse(resultData))); // for some reason insert return json as string
	}else{
		document.getElementById("insert_expense_error").innerHTML = "Please enter a valid expense";
	}
}

function submitSearchForm(formSubmitEvent) {
    
    formSubmitEvent.preventDefault();
    jQuery.get(
        "api/insert",
        jQuery("#expense_form").serialize(),
       (errorData) => ShowExpense(errorData));
}


// bind the button click to this handler function
jQuery("#expense_form").submit((event) => submitSearchForm(event));

//Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/displaytable" ,
    success: (resultData) => DisplayExpense(resultData)   // display all expense when fired up
});

 