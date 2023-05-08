$(document).ready(function() {
	$("#buttonAddToCart").on('click', function(e) {
		e.preventDefault();
		addToCart($(this));
	});
	
	
})



function addToCart(link) {
	productId = link.attr("productid");
	quantity = $("#quantity").val();
	url = contextPath + "cart/add/" + productId + "/" + quantity;
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showModal("Shopping Cart", response);
	}).fail(function(response) {
		showModal("Shopping Cart", response);
	})
}

