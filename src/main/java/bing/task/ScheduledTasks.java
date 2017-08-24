package bing.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 定时任务
 *
 * @author IceWee
 */
@Slf4j
@Component
public class ScheduledTasks {

    @Scheduled(cron = "0 0/1 * * * *")
    public void printCurrentTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        log.info("北京时间: {}", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
