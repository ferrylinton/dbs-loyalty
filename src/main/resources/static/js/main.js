$(document).ready(function () {
	
	initMainContentHeight();
	
	initInputMaterial();
	
	Sidenav.init();
	
	initFormatDate();
	
	initToast();
	
	initDeleteModal();
	
	initTaskModal();
	
	initEditor($('#content'));
	
	initEditor($('#termAndCondition'));
	
	initCustomFile();
	
	initDobMask();
	
	FormUtil.initActivated();
	
	FormUtil.initLocked();
	
	FormUtil.initShowInCarousel();

	DateUtil.startEndPeriod();
	
	DateUtil.startEndPeriodWithTime();

	DateUtil.initSearch();
	
	JsonUtil.toTable();
	
});

function initToast(){
	$('.toast').toast('show');
}

function initDobMask(){
	//$('#dob').inputmask('99-99-9999',{ 'clearIncomplete': true , alias: "datetime", inputFormat: "dd/mm/yyyy"});
	$('#dob').inputmask("99-99-9999",{ alias: "datetime", 'clearIncomplete': true, "placeholder": "dd-mm-yyyy" });
}

function initDeleteModal(){
	$('button[data-target="#delete-modal"]').click(function(e){
		$('#delete-modal-form').attr('action', $(this).attr('title'));
	});
}

function initTaskModal(){
	
	$('#verify-modal-submit').click(function(e){
		document.getElementById('verified').value = true;
		$('#task-form').submit() ;
	});
	
	$('#reject-modal-submit').click(function(e){
		document.getElementById('verified').value = false;
		$('#task-form').submit() ;
	});
	
}

function initInputMaterial(){
	$('body').materializeInputs();
}

function initFormatDate(){
	$('span[data-date]').each(function(index){
		var locale = Lang.getLocale();
		var date = $(this).text();
		var format = $(this).data('date');
		var localDate = moment.utc(date).toDate();
		var formated = moment(localDate).locale(locale).format(format);
		$(this).text(formated);
	});
}

function initEditor(obj){
	if(obj.length){
		obj.trumbowyg({
			lang: $('html').attr('lang'),
			svgPath: $('meta[name=contextPath]').attr('content') + 'static/svg/icons.svg',
			btns: [
				['viewHTML'],
				['undo', 'redo'],
				['formatting'],
				['strong', 'em', 'del'],
				['justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull'],
				['unorderedList', 'orderedList']
			],
			autogrow: true
		});
	}
}

function initMainContentHeight(){

	setMainContentHeight();

	$(window).resize(function (e) {
		setMainContentHeight();
	});

}

function setMainContentHeight(){
	if($(window).height() >= 400){
		var mainContent = $('#main-content');
		var breadcrumb = $('.breadcrumb');
		var windowWidth = $(window).width();
		var windowHeight =  $(window).height();
		
		if(breadcrumb.length){
			if (windowWidth >= 576) {
				mainContent.css('min-height', windowHeight - 175);
			}else{
				mainContent.css('min-height', windowHeight - 195);
			}
		}else{
			if (windowWidth >= 576) {
				mainContent.css('min-height', windowHeight - 150);
			}else{
				mainContent.css('min-height', windowHeight - 165);
			}
		}
	}
}

function openNewTab(el) {
    var url = $(el).attr('href');
    window.open(url, '_blank');

    return false;
}

function initCustomFile(){
	$('.custom-file input').change(function (e) {
        var files = [];
        for (var i = 0; i < $(this)[0].files.length; i++) {
            files.push($(this)[0].files[i].name);
        }
        $(this).next('.custom-file-label').html(files.join(', '));
    });
}
