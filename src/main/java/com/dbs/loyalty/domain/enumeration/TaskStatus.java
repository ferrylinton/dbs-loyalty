package com.dbs.loyalty.domain.enumeration;

public enum TaskStatus {
    All(0), 
    Pending(1), 
    Rejected(2), 
    Verified(3), 
    Failed(4);
	
	private final int value;

    private TaskStatus(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
