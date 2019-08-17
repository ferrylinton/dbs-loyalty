package com.dbs.loyalty.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SequenceUtil {

	public static String PREFIX = "DBS";
	
	public static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
	
	private static AtomicLong value = new AtomicLong(1);
	
	public static String getNext() {
        return String.format("%s%08d", getPrefix(), value.getAndIncrement());
    }
	
	public static String getPrefix() {
		LocalDate today = LocalDate.now();
        return String.format("%s%s", PREFIX, today.format(FORMATTER));
	}
	
	public static void reset(long num) {
		log.info("Reset sequence to " + num);
        value.set(num);
    }
	
	private SequenceUtil() {
		// hide constructor
	}
	
}
