
$(document).ready(function(){
	
	$(".btn-clear").click(function(e){
		e.preventDefault();
		location.replace(url);
	})
	
	$(".link-delete").on("click", function(e){
		e.preventDefault();
		link = $(this);
		$("#btn-ok").attr("href", link.attr("href"));
		showModal("Confirm", "Are you sure to delete");
	});
	
});

function showModal(title, message){
	$(".modal-title").text(title);
	$(".modal-body").text(message);
	$("#confirm-modal").modal();
}