package com.dbs.loyalty.domain.enumeration;

public enum LoginStatus {

	FAILURE(0),
	
	SUCCESS(1);

	private final int value;

    private LoginStatus(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
