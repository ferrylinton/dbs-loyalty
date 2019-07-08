var Sidenav = (function(){

    'use strict';

    function initSidenav() {
        var content = $('#content-box');
        var sidenav = $('#sidenav-box');
        var btnMenu = $('.btn-menu span');
    
        if ($(window).width() >= 768) {
            content.css('padding-right', '250px');
            sidenav.css('right', '0');
            btnMenu.removeClass('icon-menu');
            btnMenu.addClass('icon-cancel');
        } else {
            content.css('padding-right', '0');
            sidenav.css('right', '-250px');
            btnMenu.addClass('icon-menu');
            btnMenu.removeClass('icon-cancel');
        }
    }
    
    function toggle(){
        var content = $('#content-box');
        var sidenav = $('#sidenav-box');
        var btnMenu = $('.btn-menu span');

        if (sidenav.css('right') == '0px'){
            sidenav.css('right', '-250px');
            btnMenu.addClass('icon-menu');
            btnMenu.removeClass('icon-cancel');

            if ($(window).width() >= 768) {
                content.css('padding-right', '0');
            }
        }else{
            sidenav.css('right', '0');
            btnMenu.removeClass('icon-menu');
            btnMenu.addClass('icon-cancel');
            
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
            if((typeof $(this).attr('data-menu') !== 'undefined') && $(this).attr('data-menu') == $('body').attr('data-menu')){
                $(this).addClass('active');
                return true;
            }
        });
    }

    return {
        init: init,
        toggle: toggle
    }

}());