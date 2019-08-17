package com.dbs.loyalty.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ScheduledTasks {

	@Scheduled(cron = "0 0 0 * * ?")
    public void everyMidnight() {
        log.info("Cron Task :: Every Midnight");
	}
	
}
