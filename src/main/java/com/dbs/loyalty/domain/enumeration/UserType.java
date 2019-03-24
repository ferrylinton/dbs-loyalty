package com.dbs.loyalty.domain.enumeration;

public enum UserType {

	Internal(0),
	
	External(1);

	private final int value;

    private UserType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
