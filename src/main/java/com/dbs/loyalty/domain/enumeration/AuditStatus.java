package com.dbs.loyalty.domain.enumeration;

public enum AuditStatus {
    ALL(0), 
    FAILED(1),
	SUCCEEDED(2);
	
	private final int value;

    private AuditStatus(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
