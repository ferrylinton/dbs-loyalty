package com.dbs.loyalty.domain.enumeration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CustomerTypeConverter implements AttributeConverter<CustomerType, String> {
 
    @Override
    public String convertToDatabaseColumn(CustomerType customerType) {
        return customerType.getValue();
    }
 
    @Override
    public CustomerType convertToEntityAttribute(String value) {
        return CustomerType.fromValue(value);
    }

}
