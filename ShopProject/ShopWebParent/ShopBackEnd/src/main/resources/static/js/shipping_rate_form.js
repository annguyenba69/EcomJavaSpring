
$(document).ready(function(){
	dropdowCountries = $("#country");
	dropdownStates = $("#listStates");
	
	dropdowCountries.on("change", function(){
		loadStatesForCountry();
		$("#state").val("").focus();
	});
});

function loadStatesForCountry(){
	countryId = dropdowCountries.val();
	url = contextPath + "states/listStateByCountry/" + countryId;
	$.get(url, function(responseJSON){
		dropdownStates.empty();
		$.each(responseJSON, function(index, state){
			$("<option>").val(state.name).text(state.name).appendTo(dropdownStates);
		});
	}).fail(function(){
		console.log("Error");
	})
}
