package com.chatbot.chatbot.schedulers;

import com.chatbot.chatbot.models.ExaminationPeriod;
import com.chatbot.chatbot.repositories.ExaminationPeriodDao;
import com.chatbot.chatbot.services.SeleniumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DataScheduler {
    private final SeleniumService seleniumService;
    private final ExaminationPeriodDao examinationPeriodDao;

    @Autowired
    public DataScheduler(SeleniumService seleniumService, ExaminationPeriodDao examinationPeriodDao) {
        this.seleniumService = seleniumService;
        this.examinationPeriodDao = examinationPeriodDao;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @EventListener(ApplicationReadyEvent.class)
    public void scheduleTaskUsingCronExpression() {
        List<ExaminationPeriod> examinationPeriods = seleniumService.getAllExaminationPeriods();

        if (!examinationPeriods.isEmpty()) {
            examinationPeriodDao.deleteAll();
            examinationPeriodDao.saveAll(examinationPeriods);
        }

        log.info("Successfully saved {} examination periods", examinationPeriods.size());
    }

}
