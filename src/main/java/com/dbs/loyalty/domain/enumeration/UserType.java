package com.dbs.loyalty.domain.enumeration;

public enum UserType {

	INTERNAL(0),
	
	EXTERNAL(1);

	private final int value;

    private UserType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
