package com.chatbot.chatbot.schedulers;

import com.chatbot.chatbot.models.ExamRegistration;
import com.chatbot.chatbot.repositories.ExamRegistrationDao;
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
    private final ExamRegistrationDao examRegistrationDao;

    @Autowired
    public DataScheduler(SeleniumService seleniumService, ExamRegistrationDao examRegistrationDao) {
        this.seleniumService = seleniumService;
        this.examRegistrationDao = examRegistrationDao;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @EventListener(ApplicationReadyEvent.class)
    public void scheduleTaskUsingCronExpression() {
        List<ExamRegistration> examRegistrations = seleniumService.getAllExaminationPeriods();

        if (!examRegistrations.isEmpty()) {
            examRegistrationDao.deleteAll();
            examRegistrationDao.saveAll(examRegistrations);
        }

        log.info("Successfully saved {} examination periods", examRegistrations.size());
    }

}
