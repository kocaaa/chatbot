package com.chatbot.chatbot.repositories;

import com.chatbot.chatbot.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {
    Employee getEmployeeById(Long id);
}
