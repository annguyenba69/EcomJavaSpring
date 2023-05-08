$(document).ready(function() {

	$(".buttonAddToCartInWishList").on('click', function(e) {
		e.preventDefault();
		addToCartInWishList($(this));
	});

	$("#buttonAddWishList").on('click', function(e) {
		e.preventDefault();
		addToWishList($(this));
	})

	$(".btn-remove").on("click", function() {
		removeProduct($(this));
	})
})


function addToWishList(link) {
	productId = link.attr("productid");
	url = contextPath + "wishlists/add/" + productId;
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showModal("Wish List", response);
	}).fail(function(response) {
		showModal("Wish List", response);
	})
}

function addToCartInWishList(link) {
	productId = link.attr("productid");
	quantity = 1;
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


function showModal(title, message) {
	$(".modal-title").text(title);
	$(".modal-body").text(message);
	$("#modal-dialog").modal();
}