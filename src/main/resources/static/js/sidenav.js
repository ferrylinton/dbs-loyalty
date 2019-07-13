var Sidenav = (function(){

    'use strict';

    function initSidenav() {
        var sidenav = $('#sidenav-box');
        var btnMenu = $('.btn-menu span');
    
        sidenav.css('left', '-250px');
        btnMenu.addClass('icon-menu');
        btnMenu.removeClass('icon-cancel');
    }
    
    function toggle(){
        var sidenav = $('#sidenav-box');
        var btnMenu = $('.btn-menu');
        var btnMenuIcon = $('.btn-menu span');

        if (sidenav.css('left') == '0px'){
            sidenav.css('left', '-250px');
            sidenav.removeClass('shadow');
            btnMenu.css('background-color', '#000000');
            btnMenuIcon.addClass('icon-menu');
            btnMenuIcon.removeClass('icon-cancel');
        }else{
            sidenav.css('left', '0');
            sidenav.addClass('shadow');
            btnMenu.css('background-color', '#151515');
            btnMenuIcon.removeClass('icon-menu');
            btnMenuIcon.addClass('icon-cancel');
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