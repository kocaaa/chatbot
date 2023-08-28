package com.chatbot.chatbot.repositories;

import com.chatbot.chatbot.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDao extends JpaRepository<Course, Long> {
}
