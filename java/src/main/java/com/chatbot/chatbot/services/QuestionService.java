package com.chatbot.chatbot.services;

import com.chatbot.chatbot.models.PyMessage;
import com.chatbot.chatbot.models.PyResponse;
import org.json.JSONException;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
    String processResponse(PyResponse response, PyMessage message) throws JSONException;
}
