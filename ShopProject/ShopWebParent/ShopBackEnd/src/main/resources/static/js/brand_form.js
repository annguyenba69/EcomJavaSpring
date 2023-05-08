$(document).ready(function(){
	dropdownCategories = $("#categories");
	divChosenCategories = $("#chosenCategories");
	
	$(dropdownCategories).change(function(){
		divChosenCategories.empty();
		showChosenCategories();
	})
	showChosenCategories();
})

function showChosenCategories(){
	dropdownCategories.children("option:selected").each(function(){
		selectedCategory = $(this);
		categoryId = selectedCategory.val();
		categoryName = selectedCategory.text();
		divChosenCategories.append("<span class='badge badge-secondary m-1'>" + categoryName + "</span>");
	})
}