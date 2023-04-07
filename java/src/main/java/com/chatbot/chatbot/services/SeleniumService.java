package com.chatbot.chatbot.services;

import com.chatbot.chatbot.models.Employee;
import com.chatbot.chatbot.models.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeleniumService {
    List<Employee> getAllEmployees();
    List<Course> getAllCourses();
}
