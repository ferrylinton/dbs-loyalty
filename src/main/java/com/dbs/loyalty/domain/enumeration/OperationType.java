package com.dbs.loyalty.domain.enumeration;

public enum OperationType {

	Debit(0),
	
	Credit(1);

	private final int value;

    private OperationType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
	
}
