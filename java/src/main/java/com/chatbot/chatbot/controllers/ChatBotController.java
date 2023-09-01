package com.chatbot.chatbot.controllers;

import com.chatbot.chatbot.models.PyMessage;
import com.chatbot.chatbot.models.PyResponse;
import com.chatbot.chatbot.services.PythonService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
    private final PythonService pythonService;

    @Autowired
    public ChatBotController(PythonService pythonService) {
        this.pythonService = pythonService;
    }

    @PostMapping("/getMessage")
    public PyResponse getMessageFromFastApi(@RequestBody PyMessage pyMessage) throws JSONException {
        log.info("Endpoint /getMessage triggered with RequestBody [{}]", pyMessage);
        PyResponse pyResponse = pythonService.getChatBotResponse(pyMessage);
        log.info("Received response [{}] from FastAPI", pyResponse);

        return pyResponse;
    }
}
