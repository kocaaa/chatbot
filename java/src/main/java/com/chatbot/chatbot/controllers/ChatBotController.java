package com.chatbot.chatbot.controllers;

import com.chatbot.chatbot.models.PyMessage;
import com.chatbot.chatbot.models.PyResponse;
import com.chatbot.chatbot.services.PythonService;
import com.chatbot.chatbot.services.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
    private final PythonService pythonService;
    private final QuestionService questionService;

    @Autowired
    public ChatBotController(PythonService pythonService, QuestionService questionService) {
        this.pythonService = pythonService;
        this.questionService = questionService;
    }

    @PostMapping("/getMessage")
    public PyMessage getMessageFromFastApi(@RequestBody PyMessage pyMessage) throws JSONException {
        log.info("Endpoint /getMessage triggered with RequestBody [{}]", pyMessage);
        PyResponse pyResponse = pythonService.getChatBotResponse(pyMessage);
        log.info("Received response [{}] from FastAPI. Calling question service.", pyResponse);
        String response = questionService.processResponse(pyResponse, pyMessage);
        log.info("Received [{}] response from question service", response);

        pyMessage.setQuestion(response);
        return pyMessage;
    }
}
