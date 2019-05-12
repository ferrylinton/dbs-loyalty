package com.dbs.loyalty.batch;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerItem {
	
	private String id;

	private String email;
	
	private String name;
	
	private String phone;
	
	private String customerType;
	
	private String dob;
	
	private String passwordHash;

	private boolean activated;
	
	private boolean locked;
	
	private String createdBy;
	
	private Instant createdDate;
	
	private String lastModifiedBy;
	
	private Instant lastModifiedDate;
	
}
