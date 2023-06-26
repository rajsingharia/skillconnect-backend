package com.ssds.skillconnect;

import com.ssds.skillconnect.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
public class SkillconnectApplication {

	@Autowired
	private PostService postService;
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public static void main(String[] args) {
		SpringApplication.run(SkillconnectApplication.class, args);
	}



	//on every 1st and 15th of the month at 12:00 AM
	@Scheduled(cron = "0 0 0 1,15 * ?")
	public void DeleteClosedPostsOlderThan3Month() {
		logger.info("Deleting closed posts older than 3 months");
		postService.deleteClosedPostsOlderThan3Month();
	}

}
