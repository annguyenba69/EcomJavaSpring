dropdownBrand = $("#brand");
dropdownCategory = $("#category");

$(document).ready(function(){
	
	$("#short-description").richText();
	$("#full-description").richText();
	
	dropdownBrand.change(function(){
		dropdownCategory.empty();
		loadCategories();
	});
	getCategoriesForNewForm();
});

function getCategoriesForNewForm() {
	catIdField = $("#categoryId");
	editMode = false;
	
	if (catIdField.length) {
		editMode = true;
	}
	
	if (editMode) loadCategories();
}

function loadCategories(){
	brandId = dropdownBrand.val();
	url = brandModuleUrl + "/" + brandId + "/" +"categories";
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategory);
			
		});			
	});
}