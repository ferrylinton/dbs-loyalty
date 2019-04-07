var Data = (function () {

    'use strict';
    
    var targetElement = '#target-element';
    
    var element;

    function confirm(el) {
    	element = el;

        $.confirm({
            columnClass: 'col-10 col-sm-8 col-md-6 col-lg-5 col-xl-4 text-center',
            icon: 'icon-trash',
            content: Lang.message('DeleteConfirm'),
            buttons: {
                cancel: {
                    text: Lang.button('cancel'),
                    btnClass: 'btn btn-secondary px-4'
                },
                delete: {
                    text: Lang.button('delete'),
                    btnClass: 'btn btn-danger px-4',
                    action: callDelete
                }
            }
        });

        return false;
    }

    function callDelete() {
        $.ajax({
            	url: element.getAttribute('href'),
            	type: 'DELETE',
        	})
            .done(done)
            .fail(fail);
    }
    
    function submit(el) {
        element = el;
        callSave();

        return false;
    }

    function callSave() {
    	var settings = {
                type: $(element).attr('method'),
                url: $(element).attr('action'),
                dataType: 'json',
                contentType: false,
                processData: false,
                data: new FormData(element),
            	beforeSend: function(jqXHR, settings){
        			jqXHR.url = $(element).attr('action');
            	}
            }
    	
        $.ajax(settings)
            .done(done)
            .fail(fail);
    }

    function done(data, textStatus, jqXHR) {
    	if (data.resultUrl != null) {
            Alert.success(data.message, function () {
                window.location = data.resultUrl;
            });
        } else if ($(targetElement).length) {
			$(targetElement).html(data.html);
			Alert.success(data.message, function () {});
    	} else {
    		Alert.success(Lang.message('ProcessSuccess'), function () {});
    	}
    }
    
    function fail(jqXHR, textStatus, errorThrown) {
        if (jqXHR.readyState == 0) {
            Alert.error(Lang.error('ERR_CONNECTION_REFUSED'));
        } else if (jqXHR.status == 400) {
        	showValidationError(jqXHR);
        } else if (jqXHR.status == 401) {
            Alert.error(Lang.error(jqXHR.status));
            window.location.href = $('meta[name=contextPath]').attr('content') + "login";
        } else if (jqXHR.status == 403) {
            Alert.error(Lang.error(jqXHR.status));
            window.location.href = $('meta[name=contextPath]').attr('content') + "login?forbidden";
        } else if (jqXHR.status == 404 || jqXHR.status == 405 || jqXHR.status == 406) {
            Alert.error(jqXHR.url + ' : ' + Lang.error(jqXHR.status));
        } else {
        	if(jqXHR.responseJSON === undefined || jqXHR.responseJSON.message === undefined){
        		Alert.error('<span class="text-red">ERROR : ' + jqXHR.responseText + '</span>');
        	}else if(jqXHR.responseJSON.message.indexOf('uq') !== -1){
        		Alert.error('<span class="text-red">ERROR : ' + Lang.error('DuplicateEntry') + '</span><hr>' + jqXHR.responseJSON.message);
        	}else{
        		Alert.error('<span class="text-red">ERROR : ' + jqXHR.responseJSON.message + '</span>');
        	}
        }
    }
    
    function showValidationError(jqXHR) {
        var response = jqXHR.responseJSON;
    	
        $(element).find('label').removeClass('text-danger');
        $(element).find('input[type="text"]').attr('class', 'form-control');
    	$(element).find('input[type="password"]').attr('class', 'form-control');
    	$(element).find('textarea').attr('class', 'form-control');
    	$(element).find('#preview-image').attr('class', 'border bg-gray');
        
        for(var i=0; i<response.fields.length; i++){
        	$(element).find('[for="' + response.fields[i] + '"]').attr('class', 'text-danger');
        	$(element).find('input[type="text"][name="' + response.fields[i] + '"]').attr('class', 'form-control is-invalid');
        	$(element).find('input[type="password"][name="' + response.fields[i] + '"]').attr('class', 'form-control is-invalid');
        	$(element).find('textarea[name="' + response.fields[i] + '"]').attr('class', 'form-control is-invalid');
        	$(element).find('#preview-image').attr('class', 'border border-danger bg-gray');
        }
        
        Alert.error(response.message);
    }

    return {
        submit: submit,
        confirm: confirm
    }

}());