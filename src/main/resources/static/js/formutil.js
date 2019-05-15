var FormUtil = (function () {

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
    	console.log(obj.value);
        if(obj.value == 'External'){
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

}());