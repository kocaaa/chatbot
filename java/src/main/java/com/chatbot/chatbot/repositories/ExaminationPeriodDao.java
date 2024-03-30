package com.chatbot.chatbot.repositories;

import com.chatbot.chatbot.models.ExaminationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationPeriodDao extends JpaRepository<ExaminationPeriod, Long> {
}
