package com.chatbot.chatbot.controllers;

import com.chatbot.chatbot.models.Course;
import com.chatbot.chatbot.services.SeleniumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/course")
public class CourseController {
    private final SeleniumService seleniumService;

    @GetMapping("/all")
    public List<Course> getAllCourses(){
        return seleniumService.getAllCourses();
    }
}
