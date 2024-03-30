package com.chatbot.chatbot.schedulers;

import com.chatbot.chatbot.models.ExaminationPeriod;
import com.chatbot.chatbot.services.SeleniumService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataScheduler {
    private final SeleniumService seleniumService;

    public DataScheduler(SeleniumService seleniumService) {
        this.seleniumService = seleniumService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @EventListener(ApplicationReadyEvent.class)
    public void scheduleTaskUsingCronExpression() {
        List<ExaminationPeriod> examinationPeriods = seleniumService.getAllExaminationPeriods();
 
        for (ExaminationPeriod examinationPeriod : examinationPeriods) {
            System.out.println(examinationPeriod);
        }
    }

}
