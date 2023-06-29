package com.budgetbites.budgetbitesapi.services;

import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.quartz.TriggerKey.triggerKey;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final Scheduler scheduler;

    Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    public void updateJobExecutionDate(Date date) throws SchedulerException {
        Trigger existingTrigger = scheduler.getTrigger(triggerKey("ingredientUpdateJobTrigger"));

        Trigger updatedTrigger = TriggerBuilder.newTrigger()
                .forJob("ingredientUpdateJob")
                .withIdentity(existingTrigger.getKey())
                .startAt(date)
                .build();

        scheduler.rescheduleJob(existingTrigger.getKey(), updatedTrigger);
        logger.info(String.format("New job scheduled at: %s ", date));
    }
}
