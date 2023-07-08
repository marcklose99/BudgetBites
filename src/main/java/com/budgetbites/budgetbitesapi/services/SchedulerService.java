package com.budgetbites.budgetbitesapi.services;

import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;

import static org.quartz.TriggerKey.triggerKey;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final Scheduler scheduler;

    private final Trigger trigger;
    Logger logger = LoggerFactory.getLogger(SchedulerService.class);


    public void updateJobExecutionDate(LocalDateTime date) throws SchedulerException {
        Trigger updatedTrigger = TriggerBuilder.newTrigger()
                .forJob("ingredientUpdateJob")
                .withIdentity(trigger.getKey())
                .startAt(Date.from(date.atZone(ZoneId.of("America/New_York")).toInstant()))
                .build();

        scheduler.rescheduleJob(trigger.getKey(), updatedTrigger);
        logger.info(String.format("New job scheduled at: %s ", date));
    }
}
