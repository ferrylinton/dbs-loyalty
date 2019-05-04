var TaskUtil = (function () {

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

}());