package com.dbs.loyalty.domain.enumeration;

public enum TaskOperation {
    All(0), 
    Add(1), 
    Modify(2), 
    Delete(3);
    
    private final int value;

    private TaskOperation(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
