var JsonUtil = (function () {

    'use strict';

    function toTable(){
        $('div.json').each(function(){
            if($(this).text() !== ''){
                var obj = JSON.parse($(this).text());

                var table = '<table class="table table-sm table-bordered">';
                
                for (var key in obj) {
                    table += '<tr>';
                    table += '<th valign="top">' + Lang.field(key) + '</th>';
                    
                    if(Array.isArray(obj[key])){
                        var arr = obj[key];
                        
                        table += '<td>';
                        for (var i = 0; i < arr.length; i++) {
                            table += objectJsonToString(arr[i]);
                        } 
                        table += '</td>';
                    }else if(jQuery.type(obj[key]) === 'object'){
                        table += '<td>' + objectJsonToString(obj[key]) + '</td>';
                    }else if(key == 'image'){
                        var path = (obj['id'] == undefined || obj['id'] != obj[key]) ? 'image/task/' + obj[key] : 'image/' + obj[key];
                        var contextPath = $('meta[name=contextPath]').attr('content');
                        var href = (contextPath == undefined) ? path : contextPath + path;
                        var imageLink = '<a onclick="return openNewTab(this)" class="btn btn-sm btn-secondary" href="' + href + '"> <span class="icon-picture"></span> <span>' + Lang.label('viewImage') + '</span> </a>';
                        table += '<td>' + imageLink + '</td>';

                    }else if(key == 'material'){
                        var path = (obj['id'] == undefined || obj['id'] != obj[key]) ? 'pdf/task/' + obj[key] : 'pdf/' + obj[key];
                        var contextPath = $('meta[name=contextPath]').attr('content');
                        var href = (contextPath == undefined) ? path : contextPath + path;
                        var pdfLink = '<a onclick="return openNewTab(this)" class="btn btn-sm btn-secondary" href="' + href + '"> <span class="icon-file-pdf"></span> <span>' + Lang.label('viewPdf') + '</span> </a>';
                        table += '<td>' + pdfLink + '</td>';

                    }else{
                        var val = obj[key];
                        
                        if(typeof val === 'boolean' || val == '0025' || val == '0012'){
                            val = Lang.field(val);
                        }
                        
                        table += '<td>' + (val == null ? '-' : val) + '</td>';
                    }
                    
                    table += '</tr>';
                } 
                
                table += '</table>';
                $(this).html(table);
            }
        });
    }

    function objectJsonToString(obj){
        var text = '';
        text += '<table style="margin-bottom: 5px;">';
        
        for (var key in obj) {
        	text += '<tr>';
    		text += '<th>' +  Lang.field(key) + '</th>';
    		text += '<td>';
    		
    		if(jQuery.type(obj[key]) === 'object'){
        		text += objectJsonToString(obj[key]);
        	}else{
        		text += obj[key];
        	}
    		
    		text += '</td>';
    		text += '</tr>';
    		
        	
        }

        text += '</table>';
        return text;
    }

    return {
        toTable: toTable
    }

}());