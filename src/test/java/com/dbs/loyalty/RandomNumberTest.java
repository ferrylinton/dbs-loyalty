package com.dbs.loyalty;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class RandomNumberTest {

	@Test
	public void generate() {
		int count = 0;
		int total = 50;
		
		for(int i=0; i<total; i++) {
			System.out.println(RandomStringUtils.randomNumeric(8));
			count++;
		}
		
		assertEquals(count, total);
	}
	
}
