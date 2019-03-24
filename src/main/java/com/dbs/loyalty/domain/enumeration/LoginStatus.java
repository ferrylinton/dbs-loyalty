package com.dbs.loyalty.domain.enumeration;

public enum LoginStatus {

	Failed(0),
	
	Succeeded(1);

	private final int value;

    private LoginStatus(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
