package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.enums.Question;
import com.chatbot.chatbot.models.Employee;
import com.chatbot.chatbot.models.PyMessage;
import com.chatbot.chatbot.models.PyResponse;
import com.chatbot.chatbot.repositories.EmployeeDao;
import com.chatbot.chatbot.services.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chatbot.chatbot.utils.LevenshteinUtil.findMostSimilarDistance;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final EmployeeDao employeeDao;
    private static final int MIN_EMAIL_LENGTH = 5;

    public QuestionServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public String processResponse(PyResponse response, PyMessage message) {
        return processQuestion(response.getQuestion(), message.getQuestion());
    }

    private String processQuestion(Question question, String message) {
        return switch (question) {
            case EXAM_REGISTRATION -> processExamRegistration();
            case PROFESSOR_SUBJECT -> processProfessorSubject();
            case ASSISTANT_SUBJECT -> processAssistantSubject();
            case CONSULTATIONS -> processConsultations();
            case CONTACT_EMAIL -> processEmail(message);
            case OFFICE_LOCATION -> processOfficeLocation();
            default -> processUnsupportedMessage();
        };
    }



    private String processExamRegistration() {
        return null;
    }

    private String processProfessorSubject() {
        return null;
    }

    private String processAssistantSubject() {
        return null;
    }

    private String processConsultations() {
        return null;
    }

    private String processEmail(String question) {
        question = question.toLowerCase();

        String email = null;
        int minDistance = Integer.MAX_VALUE;

        List<Employee> employees = employeeDao.findAll();

        for (Employee employee : employees) {
            String[] names = employee.getName().toLowerCase().split("[\\s-]+");

            int currentDistance = 0;

            for (String name : names) {
                if (name.length() > 2){
                    currentDistance += findMostSimilarDistance(name, question);
                }
            }

            if (currentDistance < minDistance) {
                email = employee.getEmail();
                minDistance = currentDistance;
            }
        }

        return email;
    }

    private String processOfficeLocation() {
        return null;
    }

    private String processUnsupportedMessage() {
        return null;
    }
}
