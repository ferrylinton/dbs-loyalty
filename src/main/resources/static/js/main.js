$(document).ready(function () {
	
	initMainContentHeight();
	
	initInputMaterial();
	
	Sidenav.init();
	
	initFormatDate();
	
	initToast();
	
	initDeleteModal();
	
	initEditor($('#content'));
	
	initEditor($('#termAndCondition'));
	
	initCustomFile();
	
	FormUtil.initActivated();
	
	FormUtil.initLocked();
	
	FormUtil.initShowInCarousel();
	
	DateUtil.initDob();
	
	DateUtil.startEndPeriod($('input[name="startPeriod"]'), $('input[name="endPeriod"]'));
	
	DateUtil.initTimePicker($('input[name="timePeriod"]'));
	
	DateUtil.initSearch();
	
	JsonUtil.toTable();
	
});

function initToast(){
	$('#toast .toast').toast('show');
}

function initDeleteModal(){
	$('button[data-target="#delete-modal"]').click(function(e){
		console.log('... click data-target="#delete-modal"')
		console.log($(this).attr('title'));
		$('#delete-modal-form').attr('action', $(this).attr('title'));
	});
	
	$('#delete-modal').on('shown.bs.modal', function (e) {
		console.log('... shown.bs.modal');
		
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
		
		if(window.location.pathname.indexOf('home') !== -1){
			if ($(window).width() >= 576) {
				mainContent.css('min-height', $(window).height() - 150);
			}else{
				mainContent.css('min-height', $(window).height() - 165);
			}
		}else{
			if ($(window).width() >= 576) {
				mainContent.css('min-height', $(window).height() - 190);
			}else{
				mainContent.css('min-height', $(window).height() - 205);
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
