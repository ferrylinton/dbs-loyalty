$(document).ready(function() {

	$('body').materializeInputs();
	
	$('.toast').toast('show');
	
	initAuditCollapse();
	
	initTaskDataDetail();
	
	initDatePicker();
	
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

function verify(obj){
	var checkboxlabel = $('[for="' + obj.id + '"]');
	var buttonVerify = $('.btn-success');
	var buttonReject = $('.btn-danger');
	
	if(obj.checked){
		checkboxlabel.text(Lang.label('verify'));
		buttonVerify.show();
		buttonReject.hide();
	}else{
		checkboxlabel.text(Lang.label('reject'));
		buttonVerify.hide();
		buttonReject.show();
	}
}

function initTaskDataDetail(){
	$('pre').each(function(){
		if($(this).text() !== ''){
			var obj = JSON.parse($(this).text());
			var table = '<table>';
			
			for (var key in obj) {
				console.log(obj[key]);
				table += '<tr>';
				table += '<td valign="top">' + key + '</td>';
				table += '<td valign="top"> : </td>';
				
				if(Array.isArray(obj[key])){
					console.log(1);
					var arr = obj[key];
					
					table += '<td>';
					for (var i = 0; i < arr.length; i++) {
						table += objectJsonToString( arr[i]);
						table += '<br>';
					} 
					table += '</td>';
				}else if(jQuery.type(obj[key]) === 'object'){
					console.log(2);
					table += '<td>' + objectJsonToString(obj[key]) + '</td>';
				}else{
					console.log(3);
					table += '<td>' + (obj[key] == null ? '-' : obj[key]) + '</td>';
				}
				
				table += '</tr>';
			} 
			
			table += '</table>';
			$(this).html(table);
		}
	});
}

function objectJsonToString(obj){
	var text = '';
	
	for (var key in obj) {
		text += ((text === '') ? '[' : ', ') + key + '=' + obj[key];
	}
	
	return text += ']';
}

function initDatePicker(){
	
	var satePickerSettings = {
		autoHide: true,
		autoPick: true,
		format: 'dd-mm-yyyy',
		date: new Date(),
		endDate: new Date()
	}
	
	var startDate = $('input[name="sd"]').datepicker(satePickerSettings)
	.on('pick.datepicker', function (e) {
		if(endDate.datepicker('getDate') < e.date){
			endDate.datepicker('setDate', e.date);
		}
	});
	
	var endDate = $('input[name="ed"]').datepicker(satePickerSettings)
	.on('pick.datepicker', function (e) {
		if(startDate.datepicker('getDate') > e.date){
			startDate.datepicker('setDate', e.date);
		}
	});
	
}
