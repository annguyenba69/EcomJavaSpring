decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
thousandsSeparator = thousandsPointType == 'COMMA' ? ',' : '.'; 
$(document).ready(function() {
	$(".linkMinus").on("click", function() {
		decreaseQuantity($(this));
	})
	$(".linkPlus").on("click", function() {
		increaseQuantity($(this));
	})
	$(".btn-remove").on("click", function() {
		removeProduct($(this));
	})
})

function decreaseQuantity(link){
	productId = link.attr("pid");
	quantity = $("#quantity" + productId);
	newQuantity = parseInt(quantity.val()) - 1;
	if(newQuantity > 0){
		quantity.val(newQuantity);
		updateQuantity(productId, newQuantity);
	}else{
		showModal("Shopping Cart", "Minimum quantity is 1");
	}
}

function increaseQuantity(link){
	productId = link.attr("pid");
	quantity = $("#quantity" + productId);
	newQuantity = parseInt(quantity.val()) + 1;
	if(newQuantity <= 10){
		quantity.val(newQuantity);
		updateQuantity(productId, newQuantity);
	}else{
		showModal("Shopping Cart", "Maximum quantity is 10");
	}
}

function updateQuantity(productId, quantity){
	url = contextPath + "cart/update/" + productId + "/" + quantity;
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(updatedSubtotal) {
		updateSubtotal(updatedSubtotal, productId);
		updateTotal();
	}).fail(function() {
		showModal("Error","Error while updating product quantity.");
	});	
}

function updateSubtotal(updatedSubtotal, productId) {
	$(".total" + productId).text(formatCurrency(updatedSubtotal));
}
/*
function formatCurrency(amount) {
	return $.number(amount, decimalDigits, decimalSeparator, thousandsSeparator);
}*/

function updateTotal() {
	total = 0.0;
	productCount = 0;
	
	$(".total-col").each(function(index, element) {
		productCount++;
		total += parseFloat(clearCurrencyFormat(element.innerHTML));
	});
	if (productCount < 1) {
		showEmptyShoppingCart();
	} else {
		$(".total-cart").text(formatCurrency(total));	
		console.log(formatCurrency(total));	
	}
	
}

function formatCurrency(amount) {
	return $.number(amount, decimalDigits, decimalSeparator, thousandsSeparator);
}

function clearCurrencyFormat(numberString) {
	result = numberString.replaceAll(thousandsSeparator, "");
	return result.replaceAll(decimalSeparator, ".");
}

function removeProduct(link){
	url = link.attr("link");
	$.ajax({
		type: "DELETE",
		url : url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response){
		rowNumber = link.attr("row");
		removeProductHTML(rowNumber);
		updateTotal();
	}).fail(function(){
		showModal("Error","Error while updating product quantity.");
	})
}

function removeProductHTML(row){
	$("#row" + row).remove();
}


function showModal(title, message){
	$(".modal-title").text(title);
	$(".modal-body").text(message);
	$("#modal-dialog").modal();
}