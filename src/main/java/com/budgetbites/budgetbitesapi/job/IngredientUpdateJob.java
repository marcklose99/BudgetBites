package com.budgetbites.budgetbitesapi.job;


import com.budgetbites.budgetbitesapi.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class IngredientUpdateJob implements Job {

    private final IngredientService ingredientService;

    @Override
    public void execute(JobExecutionContext context) {

        LocalDateTime executionDate = ingredientService.getNextExpireDate();

         try {
            ingredientService.updateIngredientsValidity(executionDate);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }

}