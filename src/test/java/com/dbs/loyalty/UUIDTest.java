package com.dbs.loyalty;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Test;

public class UUIDTest {

	@Test
	public void generate() {
		int count = 0;
		int total = 50;
		
		for(int i=0; i<total; i++) {
			System.out.println(UUID.randomUUID().toString());
			count++;
		}
		
		assertEquals(count, total);
	}
	
}
