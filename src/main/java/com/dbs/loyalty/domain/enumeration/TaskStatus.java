package com.dbs.loyalty.domain.enumeration;

public enum TaskStatus {
    ALL(0), 
    PENDING(1), 
    REJECTED(2), 
    VERIFIED(3), 
    FAILED(4);
	
	private final int value;

    private TaskStatus(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
