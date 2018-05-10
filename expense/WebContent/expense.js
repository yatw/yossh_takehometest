 
function handleSearchResult(resultData) {

	resultData = JSON.parse(resultData);
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
 
function submitSearchForm(formSubmitEvent) {
    
    formSubmitEvent.preventDefault();
    jQuery.get(
        "api/expense",
        jQuery("#expense_form").serialize(),
        (resultData) => handleSearchResult(resultData));
}



jQuery("#expense_form").submit((event) => submitSearchForm(event));

 