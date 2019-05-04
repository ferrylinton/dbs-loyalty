var AlertUtil = (function () {

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

}());