package com.chatbot.chatbot.services;

import com.chatbot.chatbot.models.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeleniumService {
    List<Employee> getAllEmployees();
}
