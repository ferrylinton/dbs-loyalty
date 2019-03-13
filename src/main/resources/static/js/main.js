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
	$('[for="' + obj.id + '"]').text(Lang.label(obj.checked ? 'yes' : 'no'));
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
				table += '<td valign="top">' + Lang.field(key) + '</td>';
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
		text += ((text === '') ? '[' : ', ') + Lang.field(key) + ' = ' + obj[key];
	}
	
	return text += ']';
}

function initDatePicker(){
	initDatePickerLang();
	initDatePickerField();
}

function initDatePickerField(){
	var startDateInput = $('input[name="sd"]');
	var endDateInput = $('input[name="ed"]');
	var startDate;
	var endDate;

	startDate = startDateInput
		.datepicker({language: getLocale()})
		.datepicker('setDate', (getQueryParam('sd') == null || getQueryParam('sd') == '') ? null : getQueryParam('sd'))
		.on('pick.datepicker', function (e) {
			if(endDate.datepicker('getDate') < e.date){
				endDate.datepicker('setDate', e.date);
			}else if((endDateInput.val()) == ''){
				endDateInput.val(endDate.datepicker('getDate', true));
			}
		});
	
	endDate = endDateInput
		.datepicker({language: getLocale()})
		.datepicker('setDate', (getQueryParam('ed') == null || getQueryParam('ed') == '') ? null : getQueryParam('ed'))
		.on('pick.datepicker', function (e) {
			if(startDate.datepicker('getDate') > e.date){
				startDate.datepicker('setDate', e.date);
			}else if((startDateInput.val()) == ''){
				startDateInput.val(startDate.datepicker('getDate', true));
			}
		});
	
}

function initDatePickerLang(){
	$.fn.datepicker.languages['en'] = {
		autoHide: true,
		endDate: new Date(),
		format: 'dd-mm-yyyy',
		days: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
		daysShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
		daysMin: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
		months: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
		monthsShort: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
		weekStart: 0,
		startView: 0,
		yearFirst: false,
		yearSuffix: ''
	};
	
	$.fn.datepicker.languages['in'] = {
		autoHide: true,
		endDate: new Date(),
		format: 'dd-mm-yyyy',
		days: ['Minggu', 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'],
		daysShort: ['Min', 'Sen', 'Sel', 'Rab', 'Kam', 'Jum', 'Sab'],
		daysMin: ['Mi', 'Se', 'Se', 'Ra', 'Ka', 'Ju', 'Sa'],
		months: ['Januari', 'Februari', 'Maret', 'April', 'Mei', 'Juni', 'Juli', 'Agustus', 'September', 'Oktober', 'November', 'Desember'],
		monthsShort: ['Jan', 'Feb', 'Mar', 'Apr', 'Mai', 'Jun', 'Jul', 'Agu', 'Sep', 'Okt', 'Nov', 'Des'],
		weekStart: 0,
		startView: 0,
		yearFirst: false,
		yearSuffix: ''
	};
}

function getQueryParam(param) {
	var url = window.location.href;
	
	if(url.indexOf('?') !== -1 && url.indexOf('&') !== -1){
		var url = window.location.href;
		var hashes = url.split('?')[1];
		var hash = hashes.split('&');
	
		for (var i = 0; i < hash.length; i++) {
			var temp = hash[i].split("=");
			
			if(temp[0] == param){
				return temp[1];
			}
		}
	}
	
	return null;
}

function getLocale(){
	var locale = $('html').attr('lang');
	
	if (locale == undefined) {
		locale = 'in';
	}
	
	return locale;
}