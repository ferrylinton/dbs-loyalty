var ImageUtil = (function () {

    function show(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            var img = new Image();
    
            reader.onload = function (e) {
                img.src = e.target.result;
                $('#preview-image').show();
                $('#preview-image img').attr('src', e.target.result);
            }
            
            img.onload = function() {
                $('#preview-image img').width(img.width).height(img.height);
            }
            
            reader.readAsDataURL(input.files[0]);
        }else{
            $('#preview-image').hide();
        }
    }

    return {
        show: show
    }

}());