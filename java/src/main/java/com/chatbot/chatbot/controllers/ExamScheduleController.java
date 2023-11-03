package com.chatbot.chatbot.controllers;

import com.chatbot.chatbot.models.YearExams;
import com.chatbot.chatbot.services.PythonService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/schedule")
public class ExamScheduleController {

    private final PythonService pythonService;

    @Autowired
    public ExamScheduleController(PythonService pythonService) {
        this.pythonService = pythonService;
    }

    @GetMapping("/all")
    public List<YearExams> getExamSchedule() throws JSONException {
        return pythonService.getExamSchedule();
    }
}
