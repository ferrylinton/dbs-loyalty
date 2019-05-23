var DateUtil = (function () {

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
    
    function datetimepickerIcon(){
    	$.fn.datetimepicker.Constructor.Default = $.extend({}, $.fn.datetimepicker.Constructor.Default, {
        	icons: {
                time		: 'icon-clock',
                date		: 'icon-calendar',
                up			: 'icon-up-open',
                down		: 'icon-down-open',
                previous	: 'icon-left-open',
                next		: 'icon-right-open',
                today		: 'icon-calendar',
                clear		: 'icon-trash',
                close		: 'icon-canel'
            } 
        });
    }
    
    function startEndPeriodWithTime(){
    	var startPeriodDatetime = $('#startPeriodDatetime');
        var endPeriodDatetime = $('#endPeriodDatetime');
        
        if(startPeriodDatetime.length && endPeriodDatetime.length){
	    	var startElement = $('#startPeriod'); 
	    	var startMinDate = (startElement.val() == '') ? moment().startOf('day') : false;
	    	var startDefaultDate = (startElement.val() == '') ? moment().startOf('day') : moment(new Date(startElement.val())).local();
	    	
	    	var endElement = $('#endPeriod');
	    	var endMinDate = (startElement.val() == '') ? moment().set({ hour:8, minute:0, second:0, millisecond:0 }) : false;
	    	var endDefaultDate = (endElement.val() == '') ? endMinDate : moment(new Date(endElement.val())).local();
	
	    	datetimepickerIcon();
	        
	        // start date
	        startPeriodDatetime.datetimepicker({
	        	format		: $('#jsDate').length ? $('#jsDate').text() : 'DD-MM-YYYY',
	        	minDate		: startMinDate,
	        	defaultDate	: startDefaultDate,
	        	inline		: true
	        });
	        startPeriodDatetime.on('change.datetimepicker', function (e) {
	        	endPeriodDatetime.datetimepicker('minDate', e.date);
	        	setStartEndDatetimeValue(startPeriodDatetime.datetimepicker('date'), endPeriodDatetime.datetimepicker('date'));
	        });
	        
	        // end date
	        endPeriodDatetime.datetimepicker({
	        	format		: $('#jsDatetime').length ? $('#jsDatetime').text() : 'DD-MM-YYYY HH:mm',
	        	minDate		: endMinDate,
	        	defaultDate	: endDefaultDate,
	        	inline		: true,
	            sideBySide	: true,
	            stepping	: 5
	        });
	        endPeriodDatetime.on('change.datetimepicker', function (e) {
	        	startPeriodDatetime.datetimepicker('maxDate', e.date);
	        	setStartEndDatetimeValue(startPeriodDatetime.datetimepicker('date'), endPeriodDatetime.datetimepicker('date'));
	        });
	        
	        setStartEndDatetimeValue(startPeriodDatetime.datetimepicker('date'), endPeriodDatetime.datetimepicker('date'));
        }
    }
    
    function startEndPeriod(){
    	var startPeriodDate = $('#startPeriodDate');
        var endPeriodDate = $('#endPeriodDate');
        var format = $('#jsDate').length ? $('#jsDate').text() : 'DD-MM-YYYY';
        
        if(startPeriodDate.length && endPeriodDate.length){
	    	var startElement = $('#startPeriod'); 
	    	var startMinDate = (startElement.val() == '') ? moment().startOf('day') : false;
	    	var startDefaultDate = (startElement.val() == '') ? moment().startOf('day') : moment(startElement.val(), format);
	    	
	    	var endElement = $('#endPeriod');
	    	var endMinDate = (endElement.val() == '') ? moment().startOf('day') : false;
	    	var endDefaultDate = (endElement.val() == '') ? moment().startOf('day') : moment(endElement.val(), format);
	    	
	    	datetimepickerIcon();
	        
	        // start date
	        startPeriodDate.datetimepicker({
	        	format		: format,
	        	minDate		: startMinDate,
	        	defaultDate	: startDefaultDate,
	        	inline		: true
	        });
	        startPeriodDate.on('change.datetimepicker', function (e) {
	        	endPeriodDate.datetimepicker('minDate', e.date);
	        	setStartEndDateValue(startPeriodDate.datetimepicker('date'), endPeriodDate.datetimepicker('date'), format);
	        });
	        
	        // end date
	        endPeriodDate.datetimepicker({
	        	format		: format,
	        	minDate		: endMinDate,
	        	defaultDate	: endDefaultDate,
	        	inline		: true
	        });
	        endPeriodDate.on('change.datetimepicker', function (e) {
	        	startPeriodDate.datetimepicker('maxDate', e.date);
	        	setStartEndDateValue(startPeriodDate.datetimepicker('date'), endPeriodDate.datetimepicker('date'), format);
	        });
	        
	        setStartEndDateValue(startPeriodDate.datetimepicker('date'), endPeriodDate.datetimepicker('date'), format);
        }
    }
    
    function setStartEndDatetimeValue(startDate, endDate){
    	var startPeriod = $('#startPeriod');
        var endPeriod = $('#endPeriod');
        
        if(startPeriod.length && endPeriod.length){
    		startPeriod.val(startDate.toDate().toISOString());
    		endPeriod.val(endDate.toDate().toISOString());
    	}
    }
    
    function setStartEndDateValue(startDate, endDate, format){
    	var startPeriod = $('#startPeriod');
        var endPeriod = $('#endPeriod');
        
        if(startPeriod.length && endPeriod.length){
    		startPeriod.val(startDate.format(format));
    		endPeriod.val(endDate.format(format));
    	}
    }
    
    function startEndPeriodxxxxx(startDateInput, endDateInput){
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
                
                console.log(startDate.datepicker('getDate').toUTCString());
                console.log(endDate.datepicker('getDate').toUTCString());
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
                
                console.log(startDate.datepicker('getDate').toUTCString());
                console.log(endDate.datepicker('getDate').toUTCString());
            });
        
    }
    
    function initDob(){
        var dobInput = $('input[name="dob"]');
        var dobValue = moment(dobInput.val(), $('#js-date-format').text());
    
        initDatePickerLang(null, new Date());
        dobInput
            .datepicker({language: Lang.getLocale()})
            .datepicker('setDate', (dobInput.val() == null) ? new Date() : dobInput.val());
    }
    
    function initDatePickerLang(startDate, endDate){
    	var dateFormat = $('#js-date-format').length ? $('#js-date-format').text() : 'DD/MM/YYYY';
    	
        $.fn.datepicker.languages['en'] = {
            autoHide: true,
            startDate: startDate,
            endDate: endDate,
            format: dateFormat,
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
            format: dateFormat,
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
        startEndPeriodWithTime: startEndPeriodWithTime,
        initDob: initDob,
        initTimePicker: initTimePicker
    }

}());