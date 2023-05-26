package com.budgetbites.budgetbitesapi.config;

import com.budgetbites.budgetbitesapi.job.IngredientUpdateJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(IngredientUpdateJob.class)
                .withIdentity("ingredientUpdateJob")
                .storeDurably()
                .build();
    }
    @Bean
    public Trigger jobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob("ingredientUpdateJob")
                .withIdentity("ingredientUpdateJobTrigger")
                .build();
    }
}
