package com.chatbot.chatbot.services;

import com.chatbot.chatbot.models.PyMessage;
import com.chatbot.chatbot.models.PyResponse;
import com.chatbot.chatbot.models.Subject;
import com.chatbot.chatbot.models.YearExams;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PythonService {
    PyResponse getChatBotResponse(PyMessage message) throws JSONException;
    List<Subject> getAllSubjects() throws JSONException;
    List<YearExams> getExamSchedule() throws JSONException;
}
