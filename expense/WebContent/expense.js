 
function handleSearchResult(resultDataString) {
 
    resultDataJson = JSON.parse(resultDataString);

}
 
function submitSearchForm(formSubmitEvent) {
    
    formSubmitEvent.preventDefault();
    jQuery.get(
        "api/expense",
        jQuery("#expense_form").serialize(),
        (resultDataString) => handleSearchResult(resultDataString));
}



jQuery("#expense_form").submit((event) => submitSearchForm(event));

 