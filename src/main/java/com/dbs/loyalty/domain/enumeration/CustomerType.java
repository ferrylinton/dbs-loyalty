package com.dbs.loyalty.domain.enumeration;

public enum CustomerType {

	TPC("0025"),
	
	TREASURE("0012");

	private String value;

    private CustomerType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public static CustomerType fromValue(String value) {
        for (CustomerType customerType : CustomerType.values()) {
            if (customerType.value.equalsIgnoreCase(value)) {
                return customerType;
            }
        }
        
        return null;
    }
}
