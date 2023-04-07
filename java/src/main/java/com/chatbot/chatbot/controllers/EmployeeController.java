package com.chatbot.chatbot.controllers;

import com.chatbot.chatbot.models.Employee;
import com.chatbot.chatbot.services.SeleniumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/employee")
public class EmployeeController {

    private final SeleniumService seleniumService;

    @GetMapping("/all")
    public List<Employee> getAllEmployees(){
        return seleniumService.getAllEmployees();
    }
}
