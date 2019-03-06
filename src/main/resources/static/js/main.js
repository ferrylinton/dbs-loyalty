$(document).ready(function() {

	$('body').materializeInputs();
	
	$('.toast').toast('show');
	
	initAuditCollapse();
	
	initTaskDataDetail();
	
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

function changeApproval(obj){
	$('[for="' + obj.id + '"]').text(Lang.label(obj.checked ? 'approve' : 'reject'));
}

function initTaskDataDetail(){
	var taskDataDetail = $('#taskDataDetail');
	
	if(taskDataDetail.length){
		var obj = JSON.parse(taskDataDetail.text());

		var table = '<table>';
		for (var key in obj) {
			table += '<tr>';
			table += '<td valign="top">' + key + '</td>';
			table += '<td valign="top"> : </td>';
			
			if(Array.isArray(obj[key])){
				var arr = obj[key];
				
				table += '<td>';
				for (var i = 0; i < arr.length; i++) {
					var j = 0;
					
					for (var k in arr[i]) {
						if(j == 0){
							table += '[' + k + '=' + arr[i][k];
						}else{
							table += ', ' +  k + '=' + arr[i][k];
						}
						
						j++;
					}
					
					table += '] <br>';
				} 
				table += '</td>'
				
			}else{
				table += '<td>' + (obj[key] == null ? '-' : obj[key]) + '</td>';
			}
			
			table += '</tr>';
		} 
		table += '</table>';
		
		taskDataDetail.html(table);
	}
}
