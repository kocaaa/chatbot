package com.chatbot.chatbot.repositories;

import com.chatbot.chatbot.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDao extends JpaRepository<Employee, Long> {
    Employee getEmployeeById(Long id);
}
