var Sidenav = (function(){

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

}());