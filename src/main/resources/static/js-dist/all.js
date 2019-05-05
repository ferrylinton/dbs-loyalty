var Lang = (function () {
	
	'use strict';

	var i18n = {
		"en": {
			"button": {
				"cancel": "Cancel",
				"close": "Close",
				"delete": "Delete",
				"upload": "Upload",
				"browse": "Browse",
				"reject": "Reject",
				"verify": "Verify"
			},
			"field": {
				"activated": "Activated",
				"authorities": "Authorities",
				"content": "Content",
				"description": "Description",
				"email": "Email",
				"keyword": "Keyword",
				"id": "Id",
				"authenticateFromDb": "Authenticate From Db",
				"name": "Name",
				"password": "Password",
				"passwordHash": "Password",
				"role": "Role",
				"username": "Username",
				"title": "Title",
				"file" : "File",
				"orderNumber": "Order Number",
				"true": "Yes",
				"false": "No",
				"termAndCondition": "Term And Condition",
				"startPeriod": "Start Period",
				"endPeriod": "End Period",
				"showInCarousel": "Show In Carousel",
				"promoCategory": "Promo Category",
				"code": "Code",
				"locked": "Locked",
				"userType": "Type",
				"phone": "Phone",
				"customerType": "Type",
				"dob": "Date Of Birth",
				"image": "Image",
				"material": "Material",
				"place": "Place",
				"timePeriod": "Time"
			},
			"label":{
				"activated" : "Activated",
				"deactivated" : "Deactivated",
				"locked" : "Locked",
				"unlocked" : "Unlocked",
				"showInCarousel": "Show In Carousel",
				"notShowInCarousel": "Not Show In Carousel",
				"viewImage" : "View Image",
				"viewPdf" : "View Pdf"
			},
			"message": {
				"DeleteConfirm": "Delete data ?",
				"VerifyConfirm": "Verify data ?",
				"RejectConfirm": "Reject data ?",
				"ProcessSuccess": "The process was successful"
			},
			"error": {
				401: "Unauthorized",
				403: "Forbidden",
				404: "Not Found",
				405: "Method not allowed",
				406: "Not Acceptable",
				"ElementNotFound": "Element is not found",
				"ConstraintViolationException": "Data is referenced by other data",
				"ERR_CONNECTION_REFUSED": "Can not connect to server",
				"PickImage": "Pick image",
				"ImageName": "Name length min 3 chars and max 30 chars",
				"DuplicateEntry": "Duplicate data"
			}
		},
		"id": {
			"button": {
				"cancel": "Batal",
				"close": "Tutup",
				"delete": "Hapus",
				"upload": "Unggah",
				"browse": "Browse",
				"reject": "Tolak",
				"verify": "Verifikasi"
			},
			"field": {
				"activated": "Aktif",
				"authorities": "Hak Akses",
				"content": "Konten",
				"description": "Keterangan",
				"email": "Email",
				"keyword": "Kata Kunci",
				"id": "Id",
				"authenticateFromDb": "Login Dari Db",
				"name": "Nama",
				"password": "Kata Sandi",
				"passwordHash": "Kata Sandi",
				"role": "Peranan",
				"username": "Nama Pengguna",
				"title": "Judul",
				"file" : "Berkas",
				"orderNumber": "Nomor Urut",
				"true": "Ya",
				"false": "Tidak",
				"termAndCondition": "Syarat Dan Kondisi",
				"startPeriod": "Periode Mulai",
				"endPeriod": "Periode Akhir",
				"showInCarousel": "Tampilkan di Carousel",
				"promoCategory": "Kategori Promo",
				"code": "Kode",
				"locked": "Dikunci",
				"userType": "Tipe",
				"phone": "Telepon",
				"customerType": "Tipe",
				"dob": "Tanggal Lahir",
				"image": "Gambar",
				"material": "Materi",
				"place": "Tempat",
				"timePeriod": "Jam"
			},
			"label":{
				"activated" : "Aktif",
				"deactivated" : "Tidak Aktif",
				"locked" : "Terkunci",
				"unlocked" : "Tidak Terkunci",
				"showInCarousel": "Tampil Di Carousel",
				"notShowInCarousel": "Tidak Tampil Di Carousel",
				"viewImage" : "Lihat Gambar",
				"viewPdf" : "Lihat Pdf"
			},
			"message": {
				"DeleteConfirm": "Hapus data ?",
				"VerifyConfirm": "Verifikasi data ?",
				"RejectConfirm": "Tolak data ?",
				"ProcessSuccess": "Proses berhasil"
			},
			"error": {
				401: "Unauthorized",
				403: "Forbidden",
				404: "Not Found",
				405: "Method not allowed",
				406: "Not Acceptable",
				"ElementNotFound": "tidak ditemukan",
				"ConstraintViolationException": "Data masih digunakan",
				"ERR_CONNECTION_REFUSED": "Tidak bisa melakukan koneksi",
				"PickImage": "Pilih gambar",
				"ImageName": "Nama min 3 karakter dan maks 30 karakter",
				"DuplicateEntry": "Data rangkap"
			}
		}
	}

	function button(code) {
		return lang('button', code);
	}

	function message(code) {
		return lang('message', code);
	}

	function field(code) {
		return lang('field', code);
	}
	
	function label(code) {
		return lang('label', code);
	}

	function error(code) {
		return lang('error', code);
	}

	function lang(type, code) {
		var locale = $('html').attr('lang');
		var result = '';

		if (locale == undefined) {
			result = i18n['in'][type][code];
		} else if(i18n[locale] != undefined){
			result = i18n[locale][type][code];
		}

		if (result == undefined || result == '') {
			return locale + '.' + type + '.' + code;
		} else {
			return result;
		}
	}

	function getLocale(){
		var locale = $('html').attr('lang');
		
		if (locale == undefined) {
			locale = 'id';
		}
		
		return locale;
	}

	return {
		button: button,
		message: message,
		field: field,
		label: label,
		error: error,
		lang: lang,
		getLocale: getLocale
	};

}());$.fn.materializeInputs = function(selectors) {

    // default param with backwards compatibility
    if (typeof(selectors)==='undefined') selectors = "input, textarea";

    // attribute function
    function setInputValueAttr(element) {
        element.setAttribute('value', element.value);
    }

    // set value attribute at load
    this.find(selectors).each(function () {
        setInputValueAttr(this);
    });

    // on keyup
    this.on("keyup", selectors, function() {
        setInputValueAttr(this);
    });
};jconfirm.defaults = {
    title: false,
    titleClass: '',
    type: 'dark',
    typeAnimated: true,
    draggable: true,
    dragWindowGap: 15,
    dragWindowBorder: true,
    animateFromElement: true,
    smoothContent: true,
    content: 'Are you sure to continue?',
    buttons: {},
    defaultButtons: {
        ok: {
            action: function () {
            }
        },
        close: {
            action: function () {
            }
        },
    },
    contentLoaded: function (data, status, xhr) {
    },
    icon: '',
    lazyOpen: false,
    bgOpacity: null,
    theme: 'bootstrap',
    animation: 'scale',
    closeAnimation: 'scale',
    animationSpeed: 400,
    animationBounce: 1,
    rtl: false,
    container: 'body',
    containerFluid: false,
    backgroundDismiss: false,
    backgroundDismissAnimation: 'shake',
    autoClose: false,
    closeIcon: true,
    closeIconClass: false,
    watchInterval: 100,
    columnClass: 'col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 col-xs-10 col-xs-offset-1',
    boxWidth: '50%',
    scrollToPreviousElement: true,
    scrollToPreviousElementAnimate: true,
    useBootstrap: true,
    offsetTop: 40,
    offsetBottom: 40,
    bootstrapClasses: {
        container: 'container',
        containerFluid: 'container-fluid',
        row: 'row justify-content-center align-items-center',
    },
    onContentReady: function () { },
    onOpenBefore: function () { },
    onOpen: function () { },
    onClose: function () { },
    onDestroy: function () { },
    onAction: function () { }
};var Sidenav = (function(){

    'use strict';

    function initSidenav() {
        var content = $('#content-box');
        var sidenav = $('#sidenav-box');
    
        if ($(window).width() >= 768) {
            content.css('padding-right', '250px');
            sidenav.css('right', '0');
        } else {
            content.css('padding-right', '0');
            sidenav.css('right', '-250px');
        }
    }
    
    function toggle(){
        var content = $('#content-box');
        var sidenav = $('#sidenav-box');

        if (sidenav.css('right') == '0px'){
            sidenav.css('right', '-250px');

            if ($(window).width() >= 768) {
                content.css('padding-right', '0');
            }

        }else{
            sidenav.css('right', '0');
            
            if ($(window).width() >= 768) {
                content.css('padding-right', '250px');
            }

        }
    }

    function init(){
        $(window).resize(function (e) {
            initSidenav();
        });
    
        initSidenav();
        checkSideNav();
        setActive();
    }

    function checkSideNav(){
        $('.vertical-menu ul').each(function(){
            if($(this).has('li').length == 0){
                $(this).remove();
            }
        });

        $('.vertical-menu li').each(function(){
            if($(this).has('a').length == 0){
                $(this).remove();
            }
        });
    }

    function setActive(){
        $('.vertical-menu a').each(function(){
            $(this).removeClass('active');

            if(window.location.pathname.endsWith($(this).attr('rel'))){
                $(this).addClass('active');
            }
        });
    }

    return {
        init: init,
        toggle: toggle
    }

}());var AlertUtil = (function () {

    'use strict';

    function success(content, successCallback) {
        $.alert({
            type: 'green',
            columnClass: 'col-12 col-sm-12 col-md-10 col-lg-8 col-xl-6 text-center',
            icon: 'icon-ok',
            content: content,
            buttons: {
                close: {
                    text: Lang.button('close'),
                    btnClass: 'btn btn-light text-success px-4',
                    action: successCallback
                }
            },
            onClose: successCallback
        });
    }

    function error(content) {
        $.alert({
            type: 'red',
            columnClass: 'col-12 col-sm-12 col-md-10 col-lg-8 col-xl-6 text-center',
            icon: 'icon-attention',
            content: content,
            buttons: {
                close: {
                    text: Lang.button('close'),
                    btnClass: 'btn btn-light text-danger px-4'
                }
            }
        });
    }

    return {
        success: success,
        error: error
    }

}());var AjaxUtil = (function () {

    'use strict';
    
    var settings;

    function ajaxSend(){
        var token = $("meta[name='_csrf']");
        var header = $("meta[name='_csrf_header']");
    
        if(token.length && header.length){
            $(document).ajaxSend(function(e, xhr, options) {
                xhr.setRequestHeader(header.attr('content'), token.attr('content'));
            });
        }
    }

    function confirm(el) {
        settings = {
            url: el.getAttribute('href'),
            type: 'DELETE',
            beforeSend: function(jqXHR, settings){
                jqXHR.url = el.getAttribute('href');
            }
        }

        $.confirm({
            type: 'red',
            columnClass: 'col-12 col-sm-12 col-md-10 col-lg-8 col-xl-6 text-center',
            icon: 'icon-trash',
            content: Lang.message('DeleteConfirm'),
            buttons: {
                cancel: {
                    text: Lang.button('cancel'),
                    btnClass: 'btn btn-light px-4'
                },
                delete: {
                    text: Lang.button('delete'),
                    btnClass: 'btn btn-light text-danger px-4',
                    action: callDelete
                }
            }
        });

        return false;
    }

    function callDelete() {
        ajaxSend();
        $.ajax(settings)
            .done(done)
            .fail(fail);
    }
    
    function submit(el) {
        settings = {
            type: $(el).attr('method'),
            url: $(el).attr('action'),
            dataType: 'json',
            contentType: false,
            processData: false,
            data: new FormData(el),
            beforeSend: function(jqXHR, settings){
                jqXHR.url = $(el).attr('action');
            }
        }

        callSave();
        return false;
    }

    function callSave() {
        ajaxSend();
        $.ajax(settings)
            .done(done)
            .fail(fail);
    }

    function done(data, textStatus, jqXHR) {
        var resultUrl = settings.url;
        if(settings.type == 'DELETE'){
            resultUrl = removeLastPathVariable(resultUrl);
        }

        AlertUtil.success(data.message, function () {
            window.location = resultUrl;
        });
    }
    
    function fail(jqXHR, textStatus, errorThrown) {
        if (jqXHR.readyState == 0) {
            AlertUtil.error(Lang.error('ERR_CONNECTION_REFUSED'));
        } else if (jqXHR.status == 400) {
        	showValidationError(jqXHR);
        } else if (jqXHR.status == 401) {
            AlertUtil.error(Lang.error(jqXHR.status));
            window.location.href = $('meta[name=contextPath]').attr('content') + "login";
        } else if (jqXHR.status == 403) {
            AlertUtil.error(Lang.error(jqXHR.status));
            window.location.href = $('meta[name=contextPath]').attr('content') + "login?forbidden";
        } else if (jqXHR.status == 404 || jqXHR.status == 405 || jqXHR.status == 406) {
            AlertUtil.error(jqXHR.url + ' : ' + Lang.error(jqXHR.status));
        } else {
        	if(jqXHR.responseJSON === undefined || jqXHR.responseJSON.message === undefined){
        		AlertUtil.error('<span class="text-red">ERROR : ' + jqXHR.responseText + '</span>');
        	}else if(jqXHR.responseJSON.message.indexOf('uq') !== -1){
        		AlertUtil.error('<span class="text-red">ERROR : ' + Lang.error('DuplicateEntry') + '</span><hr>' + jqXHR.responseJSON.message);
        	}else{
        		AlertUtil.error('<span class="text-red">ERROR : ' + jqXHR.responseJSON.message + '</span>');
        	}
        }
    }
    
    function showValidationError(jqXHR) {
        var response = jqXHR.responseJSON;
    	
        $('label').removeClass('text-danger');
        $('input[type="text"]').attr('class', 'form-control');
    	$('input[type="password"]').attr('class', 'form-control');
    	$('textarea').not('.trumbowyg-textarea').attr('class', 'form-control');
        
        for(var i=0; i<response.fields.length; i++){
        	$('[for="' + response.fields[i] + '"]').attr('class', 'text-danger');
        	$('input[type="text"][name="' + response.fields[i] + '"]').attr('class', 'form-control is-invalid');
        	$('input[type="password"][name="' + response.fields[i] + '"]').attr('class', 'form-control is-invalid');
        	$('textarea[name="' + response.fields[i] + '"]').not('.trumbowyg-textarea').attr('class', 'form-control is-invalid');
        }
        
        AlertUtil.error(response.message);
    }

    function removeLastPathVariable(url){
        var arr = url.split('/');
        arr.pop();
        return( arr.join('/') );
    }

    return {
        submit: submit,
        confirm: confirm
    }

}());var TaskUtil = (function () {

    function verify(){
        $.confirm({
            type: 'green',
            columnClass: 'col-12 col-sm-12 col-md-10 col-lg-8 col-xl-6 text-center',
            icon: 'icon-ok',
            content: Lang.message('VerifyConfirm'),
            buttons: {
                cancel: {
                    text: Lang.button('cancel'),
                    btnClass: 'btn btn-light px-4'
                },
                verify: {
                    text: Lang.button('verify'),
                    btnClass: 'btn btn-light text-success px-4',
                    action: callVerify
                }
            }
        });
    }

    function reject(){
        $.confirm({
            type: 'red',
            columnClass: 'col-12 col-sm-12 col-md-10 col-lg-8 col-xl-6 text-center',
            icon: 'icon-block',
            content: Lang.message('RejectConfirm'),
            buttons: {
                cancel: {
                    text: Lang.button('cancel'),
                    btnClass: 'btn btn-light px-4'
                },
                verify: {
                    text: Lang.button('reject'),
                    btnClass: 'btn btn-light text-danger px-4',
                    action: callReject
                }
            }
        });
    }
   
    function callVerify(){
        document.getElementById('verified').value = true;
        AjaxUtil.submit(document.getElementById('task-form'));
    }

    function callReject(){
        document.getElementById('verified').value = false;
        AjaxUtil.submit(document.getElementById('task-form'));
    }

    return {
        verify: verify,
        reject: reject
    }

}());var JsonUtil = (function () {

    'use strict';

    function toTable(){
        $('div.json').each(function(){
            if($(this).text() !== ''){
                var obj = JSON.parse($(this).text());

                var table = '<table class="table table-sm table-bordered">';
                
                for (var key in obj) {
                    table += '<tr>';
                    table += '<th valign="top">' + Lang.field(key) + '</th>';
                    
                    if(Array.isArray(obj[key])){
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
                    }else if(key == 'image'){
                        var path = (obj['id'] == undefined || obj['id'] != obj[key]) ? 'image/task/' + obj[key] : 'image/' + obj[key];
                        var contextPath = $('meta[name=contextPath]').attr('content');
                        var href = (contextPath == undefined) ? path : contextPath + path;
                        var imageLink = '<a onclick="return openNewTab(this)" class="btn btn-sm btn-secondary" href="' + href + '"> <span class="icon-picture"></span> <span>' + Lang.label('viewImage') + '</span> </a>';
                        table += '<td>' + imageLink + '</td>';

                    }else if(key == 'material'){
                        var path = (obj['id'] == undefined || obj['id'] != obj[key]) ? 'pdf/task/' + obj[key] : 'pdf/' + obj[key];
                        var contextPath = $('meta[name=contextPath]').attr('content');
                        var href = (contextPath == undefined) ? path : contextPath + path;
                        var pdfLink = '<a onclick="return openNewTab(this)" class="btn btn-sm btn-secondary" href="' + href + '"> <span class="icon-file-pdf"></span> <span>' + Lang.label('viewPdf') + '</span> </a>';
                        table += '<td>' + pdfLink + '</td>';

                    }else{
                        var val = obj[key];
                        
                        if(typeof val === 'boolean'){
                            val = Lang.field(val);
                        }
                        
                        table += '<td>' + (val == null ? '-' : val) + '</td>';
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
        
        text += ']';
        return text;
    }

    return {
        toTable: toTable
    }

}());var DateUtil = (function () {

    'use strict';

    function initSearch(){
        var startDateInput = $('input[name="sd"]');
        var endDateInput = $('input[name="ed"]');
        var startDate;
        var endDate;
    
        initDatePickerLang(null, new Date());
        startDate = startDateInput
            .datepicker({language: Lang.getLocale()})
            .datepicker('setDate', (getQueryParam('sd') == null || getQueryParam('sd') == '') ? null : getQueryParam('sd'))
            .on('pick.datepicker', function (e) {
                if(endDate.datepicker('getDate') < e.date){
                    endDate.datepicker('setDate', e.date);
                }else if((endDateInput.val()) == ''){
                    endDateInput.val(endDate.datepicker('getDate', true));
                }
            });
        
        endDate = endDateInput
            .datepicker({language: Lang.getLocale()})
            .datepicker('setDate', (getQueryParam('ed') == null || getQueryParam('ed') == '') ? null : getQueryParam('ed'))
            .on('pick.datepicker', function (e) {
                if(startDate.datepicker('getDate') > e.date){
                    startDate.datepicker('setDate', e.date);
                }else if((startDateInput.val()) == ''){
                    startDateInput.val(startDate.datepicker('getDate', true));
                }
            });
        
    }
    
    function startEndPeriod(startDateInput, endDateInput){
        var startDate;
        var endDate;
    
        initDatePickerLang(new Date(), null);
        startDate = startDateInput
            .datepicker({language: Lang.getLocale()})
            .datepicker('setDate', (startDateInput.val() == null) ? new Date() : startDateInput.val())
            .on('pick.datepicker', function (e) {
                if(endDate.datepicker('getDate') < e.date){
                    endDate.datepicker('setDate', e.date);
                }else if((endDateInput.val()) == ''){
                    endDateInput.val(endDate.datepicker('getDate', true));
                }
            });
        
        endDate = endDateInput
            .datepicker({language: Lang.getLocale()})
            .datepicker('setDate', (endDateInput.val() == null) ? new Date() : endDateInput.val())
            .on('pick.datepicker', function (e) {
                if(startDate.datepicker('getDate') > e.date){
                    startDate.datepicker('setDate', e.date);
                }else if((startDateInput.val()) == ''){
                    startDateInput.val(startDate.datepicker('getDate', true));
                }
            });
        
    }
    
    function initDob(){
        var dobInput = $('input[name="dob"]');
    
        initDatePickerLang(null, new Date());
        dobInput
            .datepicker({language: Lang.getLocale()})
            .datepicker('setDate', (dobInput.val() == null) ? new Date() : dobInput.val());
    }
    
    function initDatePickerLang(startDate, endDate){
        $.fn.datepicker.languages['en'] = {
            autoHide: true,
            startDate: startDate,
            endDate: endDate,
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
        
        $.fn.datepicker.languages['id'] = {
            autoHide: true,
            startDate: startDate,
            endDate: endDate,
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

    function initTimePicker(timeInput){
        var defaultTime = timeInput.val() == '' ? '08:00' : timeInput.val();

        timeInput.timepicker({
            timeFormat: 'HH:mm',
            interval: 60,
            minTime: '01',
            maxTime: '23:00',
            defaultTime: defaultTime,
            startTime: '01:00',
            dynamic: false,
            dropdown: true,
            scrollbar: true,
            zindex: 20
        });
    }

    return {
        initSearch: initSearch,
        startEndPeriod: startEndPeriod,
        initDob: initDob,
        initTimePicker: initTimePicker
    }

}());var ImageUtil = (function () {

    function show(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            var img = new Image();
    
            reader.onload = function (e) {
                img.src = e.target.result;
                $('#preview-image').show();
                $('#preview-image img').attr('src', e.target.result);
            }
            
            img.onload = function() {
                $('#preview-image img').width(img.width).height(img.height);
            }
            
            reader.readAsDataURL(input.files[0]);
        }else{
            $('#preview-image').hide();
        }
    }

    return {
        show: show
    }

}());var FormUtil = (function () {

    function initLocked(){
        var locked = $('#locked');

        setLockedLabel(locked.attr('checked'));

        locked.change(function (e) {
            setLockedLabel(e.target.checked);
        });
    }

    function setLockedLabel(checked){
        $('[for="locked"]').text(Lang.label(checked ? 'locked' : 'unlocked'));
    }


    function initShowInCarousel(){
        var showInCarousel = $('#showInCarousel');

        setShowInCarouselLabel(showInCarousel.attr('checked'));

        showInCarousel.change(function (e) {
            setShowInCarouselLabel(e.target.checked);
        });
    }

    function setShowInCarouselLabel(checked){
        $('[for="showInCarousel"]').text(Lang.label(checked ? 'showInCarousel' : 'notShowInCarousel'));
    }

    function initActivated(){
        var activated = $('#activated');

        setActivatedLabel(activated.attr('checked'));

        activated.change(function (e) {
            setActivatedLabel(e.target.checked);
        });
    }

    function setActivatedLabel(checked){
        $('[for="activated"]').text(Lang.label(checked ? 'activated' : 'deactivated'));
    }

    function onChangeUserType(obj){
        if(obj.value == 'EXTERNAL'){
            $('input[type=password]').removeAttr('disabled');
        }else{
            $('input[type=password]').attr('disabled','disabled');
        }
    }

    return {
        initLocked: initLocked,
        initShowInCarousel: initShowInCarousel,
        initActivated: initActivated,
        onChangeUserType: onChangeUserType
    }

}());$(document).ready(function () {
	
	initMainContentHeight();
	
	initInputMaterial();
	
	Sidenav.init();
	
	initFormatDate();
	
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
