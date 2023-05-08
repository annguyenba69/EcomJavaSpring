$(document).ready(function(){
	
	$(".btn-cancel").click(function(e){
		e.preventDefault();
		window.location = contextPath;
	});
	
	$("#fileImage").change(function(){
		if(!checkFileSize(this)){
			return;
		}
		showImage(this);
	});
	
});

function showImage(fileInput){
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e){
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function checkFileSize(fileInput){
	var fileSize = fileInput.files[0].size;
	
	if(fileSize > MAX_FILE_SIZE){
		fileInput.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + " bytes!");
		fileInput.reportValidity();
		return false;
	}else{
		fileInput.setCustomValidity("");
		return true;
	}
}

function showModal(title, message){
	$(".modal-title").text(title);
	$(".modal-body").text(message);
	$("#modal-dialog").modal();
}

