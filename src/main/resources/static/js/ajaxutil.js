var AjaxUtil = (function () {

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

}());