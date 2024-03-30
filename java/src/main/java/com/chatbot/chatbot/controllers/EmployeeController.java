package com.chatbot.chatbot.controllers;

import com.chatbot.chatbot.models.Employee;
import com.chatbot.chatbot.repositories.EmployeeDao;
import com.chatbot.chatbot.services.SeleniumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/employees")
public class EmployeeController {

    private final SeleniumService seleniumService;
    private final EmployeeDao employeeDao;

    @GetMapping("/all")
    public List<Employee> getAllEmployees() {
        return seleniumService.getAllEmployees();
    }

    @PostMapping("/populate")
    public List<Employee> populateEmployees() {
        List<Employee> employees = seleniumService.getAllEmployees();

        if (!employees.isEmpty()) {
            log.info("Successfully scraped {} employees from site. Deleting old ones and inserting them into database", employees.size());
            employeeDao.deleteAll();
            employeeDao.saveAll(employees);
        } else {
            log.error("Endpoint /employees/populate would delete all employees, but wouldn't insert any");
        }

        return employees;
    }
}
