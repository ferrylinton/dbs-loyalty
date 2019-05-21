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
        initDob: initDob,
        initTimePicker: initTimePicker
    }

}());