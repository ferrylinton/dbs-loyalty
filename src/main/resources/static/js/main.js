$(document).ready(function () {
	
	initMainContentHeight();
	Sidenav.init();
	initFormatDate();
});

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

function onLoadLoginForm(){
	$('body').materializeInputs();
}

function onLoadUserForm(){
	FormUtil.initActivated();
	FormUtil.initLocked();
}

function onLoadCustomerForm(){
	FormUtil.initActivated();
	FormUtil.initLocked();
	DateUtil.initDob();
}

function onLoadPromoForm(){
	FormUtil.initActivated();
	FormUtil.initShowInCarousel();
	DateUtil.startEndPeriod($('input[name="startPeriod"]'), $('input[name="endPeriod"]'));
	initEditor($('#content'));
	initEditor($('#termAndCondition'));
	initCustomFile();
}

function onLoadEventForm(){
	DateUtil.startEndPeriod($('input[name="startPeriod"]'), $('input[name="endPeriod"]'));
	DateUtil.initTimePicker($('input[name="timePeriod"]'));
	initEditor($('#content'));
	initCustomFile();
}

function initEditor(obj){
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

function onLoadTask(){
	JsonUtil.toTable();
}

function onLoadLogLogin(){
	DateUtil.initSearch();
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
