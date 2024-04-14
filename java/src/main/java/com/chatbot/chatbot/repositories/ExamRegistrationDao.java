package com.chatbot.chatbot.repositories;

import com.chatbot.chatbot.models.ExamRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRegistrationDao extends JpaRepository<ExamRegistration, Long> {

    @Query("SELECT e FROM ExamRegistration e WHERE e.month LIKE %:month%")
    ExamRegistration findByMonthContaining(String month);

}
