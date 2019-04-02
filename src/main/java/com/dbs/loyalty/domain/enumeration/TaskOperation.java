package com.dbs.loyalty.domain.enumeration;

public enum TaskOperation {
    ALL(0), 
    ADD(1), 
    MODIFY(2), 
    DELETE(3);
    
    private final int value;

    private TaskOperation(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
