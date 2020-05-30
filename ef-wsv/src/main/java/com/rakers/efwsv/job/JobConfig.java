package com.rakers.efwsv.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JobConfig {
    //30分钟一次
    public static final int PERIOD = 1800;
    public static final int DELAY_SECONDS = 30;

    @Bean
    public JobDetail jobDetail(){
        return JobBuilder.newJob(SyncJob.class).storeDurably().build();
    }

    @Bean
    public Trigger jobTrigger(){
        SimpleScheduleBuilder sdb = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(PERIOD)
                .repeatForever();

        Date startTime = DateBuilder.nextGivenSecondDate(null, DELAY_SECONDS);
        return TriggerBuilder.newTrigger()
                .forJob(this.jobDetail())
                .withSchedule(sdb)
                .startAt(startTime)
                .build();

    }
}
