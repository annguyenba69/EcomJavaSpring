
rating = "";
$(document).ready(function() {
	$("#buttonComment").on('click', function() {
		addComment();
	});

	$(':radio').change(function() {
		rating = this.value;
	});
})

function addComment() {
	if (rating != "") {
		headLine = $("#headline").val();
		comment = $("#comment").val();
		productId = $("#productId").val();
		url = contextPath + "review/add";
		data = {headLine : headLine, comment : comment, rating : rating, productId : productId };
		$.ajax({
			type: "POST",
			url: url,
			data: data,
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfValue);
			},
		}).done(function(response) {
			showModal("Success", "Thank you for your comment, your comment will be moderated by the administrator");
		}).fail(function(response) {
			showModal("Error", response);
		})
	} else {
		showModal("Warning", "Please rating before submitting the form");
	}

}
