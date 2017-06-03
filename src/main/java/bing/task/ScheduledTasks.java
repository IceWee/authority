package bing.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 * 
 * @author IceWee
 */
@Component
public class ScheduledTasks {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 H点m分s秒");

	@Scheduled(cron = "*/10 * * * * *")
	public void reportCurrentTime() {
		LOGGER.info("北京时间： {}", dateFormat.format(new Date()));
	}

}
