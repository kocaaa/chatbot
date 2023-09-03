package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.enums.Question;
import com.chatbot.chatbot.models.Employee;
import com.chatbot.chatbot.models.PyMessage;
import com.chatbot.chatbot.models.PyResponse;
import com.chatbot.chatbot.repositories.EmployeeDao;
import com.chatbot.chatbot.services.QuestionService;
import com.chatbot.chatbot.utils.LatinUtil;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chatbot.chatbot.utils.LevenshteinUtil.findMostSimilarDistance;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final String UNSUPPORTED_QUESTION = "Ne umem da odgovorim na ovo pitanje";
    private final EmployeeDao employeeDao;

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
            case CONSULTATIONS -> processConsultations(message);
            case CONTACT_EMAIL -> predictEmail(message);
            case OFFICE_LOCATION -> processOfficeLocation(message);
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

    private String processConsultations(String question) {
        Employee predictedEmployee = predictEmployee(question);
        String predictedConsultations;

        if (predictedEmployee != null && !predictedEmployee.getConsultation().isBlank() && !predictedEmployee.getName().isBlank()) {
            predictedConsultations = predictedEmployee.getName() + " drzi konsultacije u sledecim terminima: " +  predictedEmployee.getConsultation();
        } else if (predictedEmployee != null && predictedEmployee.getName() != null) {
            predictedConsultations = "Nisam uspeo da pronadjem termin konsultacija kod " +  predictedEmployee.getName();
        } else {
            predictedConsultations = "Nisam uspeo da pronadjem validan termin konsultacija.";
        }

        return predictedConsultations;
    }

    private String predictEmail(String question) {
        Employee predictedEmployee = predictEmployee(question);
        String predictedEmail;

        if (predictedEmployee != null && !predictedEmployee.getEmail().isBlank() && !predictedEmployee.getName().isBlank()) {
            predictedEmail = "Email od " + predictedEmployee.getName() + " je " +  predictedEmployee.getEmail();
        } else if (predictedEmployee != null && predictedEmployee.getName() != null) {
            predictedEmail = "Nisam uspeo da pronadjem email od " +  predictedEmployee.getName() +  ".";
        } else {
            predictedEmail = "Nisam uspeo da pronadjem validan email.";
        }

        return predictedEmail;
    }

    private Employee predictEmployee(String question) {
        Employee predictedEmployee = null;
        int minDistance = Integer.MAX_VALUE;

        List<Employee> employees = employeeDao.findAll();

        question = LatinUtil.convertToLowerAlphabet(question);

        for (Employee employee : employees) {
            String[] names = LatinUtil.convertToLowerAlphabet(employee.getName()).split("[\\s-]+");

            int currentDistance = 0;

            for (String name : names) {
                if (name.length() > 2) {
                    currentDistance += findMostSimilarDistance(name, question);
                }
            }

            if (currentDistance < minDistance) {
                predictedEmployee = employee;
                minDistance = currentDistance;
            }
        }

        return predictedEmployee;
    }

    private String processOfficeLocation(String question) {
        Employee predictedEmployee = predictEmployee(question);
        String predictedOfficeLocation;

        if (predictedEmployee != null && !predictedEmployee.getCabinet().isBlank() && !predictedEmployee.getName().isBlank()) {
            predictedOfficeLocation = predictedEmployee.getName() + " mozete naći u kabinetu " +  predictedEmployee.getCabinet();
        } else if (predictedEmployee != null && predictedEmployee.getName() != null) {
            predictedOfficeLocation = "Nisam uspeo da pronadjem kabinet od " +  predictedEmployee.getName() +  ".";
        } else {
            predictedOfficeLocation = "Nisam uspeo da pronadjem validan kabinet.";
        }

        return predictedOfficeLocation;
    }

    private String processUnsupportedMessage() {
        return UNSUPPORTED_QUESTION;
    }
}
