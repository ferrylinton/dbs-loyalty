package com.dbs.loyalty;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.devskiller.friendly_id.FriendlyId;

public class IdTest {

	@Test
	public void generate() {
		int count = 0;
		int total = 50;
		
		for(int i=0; i<total; i++) {
			System.out.println(FriendlyId.createFriendlyId());
			count++;
		}
		
		assertEquals(count, total);
	}
	
}
