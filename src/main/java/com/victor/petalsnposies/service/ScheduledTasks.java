package com.victor.petalsnposies.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class ScheduledTasks {
	
	@Scheduled(cron="0 20 14 * * 0-6")
	public void cleanOfRecords() {
		System.out.println("Time for clean up");
	}
}
