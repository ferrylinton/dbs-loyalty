$(document).ready(function() {

	$('body').materializeInputs();
	
	initAuditCollapse();
	
});

function initAuditCollapse(){
	var audit = $('#audit');
	var icon = $('.btn[data-toggle="collapse"] i');
	
	audit.on('show.bs.collapse', function () {
		icon.attr('class', 'icon-down-dir float-right');
	})
		
	audit.on('hide.bs.collapse', function () {
		icon.attr('class', 'icon-up-dir float-right');
	})
}

function changeStatus(obj){
	$('[for="' + obj.id + '"]').text(Lang.label(obj.checked ? 'activated' : 'deactivated'));
}
