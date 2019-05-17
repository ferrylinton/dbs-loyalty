package com.dbs.loyalty.config.constant;

public final class CustomerTypeConstant {

	public static final String TPC_VALUE		= "0025";
	
	public static final String TREASURE_VALUE	= "0012";
	
	public static final String TPC				= "TPC";
	
	public static final String TREASURE			= "TREASURE";
	
	public static String getCustomerType(String value) {
		if(TPC_VALUE.equals(value)) {
			return TPC;
		}else {
			return TREASURE;
		}
	}

	private CustomerTypeConstant() {
		// hide constructor
	}
	
}
