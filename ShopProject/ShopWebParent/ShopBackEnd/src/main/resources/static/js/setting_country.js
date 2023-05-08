
$(document).ready(function(){
	
	buttonLoad = $("#buttonLoad");
	dropDownCountries = $("#dropDownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
	labelCountryName = $("#labelCountryName");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	
	buttonLoad.on('click', function(){
		dropDownCountries.empty();
		loadCountries();
	})
	
	buttonAddCountry.click(function(){
		if(buttonAddCountry.val() == "Add"){
			addCountry();
		}else{
			changeFormStateToNewCountry();
		}
	});
	
	dropDownCountries.on("change", function(){
		changeFormStateToSelectedCountry();
	});
	
	buttonUpdateCountry.click(function(){
		updateCountry();
	});
	
	buttonDeleteCountry.click(function(){
		deleteCountry();
	});
});

function deleteCountry(){
	optionValue = dropDownCountries.val();
	countryId = optionValue.split("-")[0];
	
	url = contextPath + "countries/delete/" + countryId;
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(header, token);
		},
	}).done(function(response){
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeFormStateToNewCountry();
		console.log(response);
	}).fail(function(){
		console.log("ERROR: Could not connect to server or server encountered an error");
	});
}


function addCountry(){
	urlAddCountry = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = {name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: urlAddCountry, 
		beforeSend: function(xhr){
			xhr.setRequestHeader(header, token);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		console.log("The new country has been added");
	}).fail(function(){
		console.log("ERROR: Could not connect to server or server encountered an error");
	});
}

function selectNewlyAddedCountry(countryId, countryCode, countryName){
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountries);
	
	$("#dropDownCountries option[value='" + optionValue +"']").prop("selected", true);
	
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function loadCountries(){
	urlLoadCountries = contextPath + "countries/listAll";
	$.get(urlLoadCountries, function(responseJSON){
		$.each(responseJSON, function(index, country){
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountries);
		});
	}).done(function(){
		buttonLoad.val("Refresh Country List");
		console.log("done");
	}).fail(function(){
		console.log("fail");
	});
}

function changeFormStateToNewCountry(){
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");
	
	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);
	
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function changeFormStateToSelectedCountry(){
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);
	
	labelCountryName.text("Selected Country:");
	
	var selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);
	
	var countryCode = dropDownCountries.val().split("-")[1];
	fieldCountryCode.val(countryCode);
}

function updateCountry(){
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	countryId = dropDownCountries.val().split("-")[0];
	
	jsonData = {id: countryId, name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(header, token);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		console.log("The country has been updated");
		
		changeFormStateToNewCountry();
	}).fail(function(){
		console.log("ERROR: Could not connect to server or server encountered an error");
	});
}
