package com.dbs.loyalty.web.customeditor;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.dbs.loyalty.config.constant.DateConstant;

public class LocalDateEditor extends PropertyEditorSupport{

	@Override
    public void setAsText(String text) throws IllegalArgumentException{
    	setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE)));
    }

    @Override
    public String getAsText() throws IllegalArgumentException {
    	if(getValue() != null) {
    		return DateTimeFormatter.ofPattern(DateConstant.JAVA_DATE).format((LocalDate) getValue());
    	}else {
    		return null;
    	}
    }
    
}
