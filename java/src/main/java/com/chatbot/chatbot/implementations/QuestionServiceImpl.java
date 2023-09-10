package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.enums.Question;
import com.chatbot.chatbot.models.*;
import com.chatbot.chatbot.repositories.EmployeeDao;
import com.chatbot.chatbot.services.PythonService;
import com.chatbot.chatbot.services.QuestionService;
import com.chatbot.chatbot.utils.LatinUtil;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chatbot.chatbot.utils.LevenshteinUtil.findMostSimilarDistance;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final String UNSUPPORTED_QUESTION = "Ne umem da odgovorim na ovo pitanje";
    private static final int MINIMAL_NAME_LENGTH = 2;
    private static final int NO_MINIMAL_LENGTH = 0;
    private final PythonService pythonService;
    private final EmployeeDao employeeDao;

    public QuestionServiceImpl(PythonService pythonService, EmployeeDao employeeDao) {
        this.pythonService = pythonService;
        this.employeeDao = employeeDao;
    }

    @Override
    public String processResponse(PyResponse response, PyMessage message) throws JSONException {
        return processQuestion(response.getQuestion(), message.getQuestion());
    }

    private String processQuestion(Question question, String message) throws JSONException {
        return switch (question) {
            case EXAM_REGISTRATION -> processExamRegistration();
            case PROFESSOR_SUBJECT -> processProfessorSubject(message);
            case ASSISTANT_SUBJECT -> processAssistantSubject(message);
            case CONSULTATIONS -> processConsultations(message);
            case CONTACT_EMAIL -> predictEmail(message);
            case OFFICE_LOCATION -> processOfficeLocation(message);
            default -> processUnsupportedMessage();
        };
    }

    private String processExamRegistration() {
        return null;
    }

    private String processProfessorSubject(String question) throws JSONException {
        Subject predictedSubject = predictSubject(question);
        String predictedProfessor;

        if (predictedSubject != null) {
            predictedProfessor = "Predmet " + predictedSubject.getName() + " predaje " +  predictedSubject.getProfessor();
        } else {
            predictedProfessor = "Nisam uspeo da pronadjem informacije o predmetu.";
        }

        return predictedProfessor;
    }

    private Subject predictSubject(String question) throws JSONException {
        Subject predictedSubject = null;
        int minDistance = Integer.MAX_VALUE;
        question = LatinUtil.convertToLowerAlphabet(question);

        List<Subject> subjects = pythonService.getAllSubjects();

        for (Subject subject : subjects) {
            int currentDistance = calculateCurrentDistance(question, subject.getName(), NO_MINIMAL_LENGTH);
            int initialDistance = calculateCurrentDistance(question, subject.getInitials(), NO_MINIMAL_LENGTH);

            if (currentDistance < minDistance || initialDistance == 0) {
                predictedSubject = subject;
                minDistance = initialDistance == 0 ? initialDistance : currentDistance;
            }
        }

        return predictedSubject;
    }

    private String processAssistantSubject(String question) throws JSONException {
        Subject predictedSubject = predictSubject(question);
        String predictedAssistants;

        if (predictedSubject != null) {
            predictedAssistants = "Asistenti na " + predictedSubject.getName() + " su " +  String.join(", ", predictedSubject.getAssistants());
        } else {
            predictedAssistants = "Nisam uspeo da pronadjem asistente na predmetu.";
        }

        return predictedAssistants;
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
            int currentDistance = calculateCurrentDistance(question, employee.getName(), MINIMAL_NAME_LENGTH);

            if (currentDistance < minDistance) {
                predictedEmployee = employee;
                minDistance = currentDistance;
            }
        }

        return predictedEmployee;
    }

    private static int calculateCurrentDistance(String question, String stringToFind, int minLength) {
        int currentDistance = 0;
        String[] names = LatinUtil.convertToLowerAlphabet(stringToFind).split("[\\s-]+");

        for (String name : names) {
            if (name.length() > minLength) {
                currentDistance += findMostSimilarDistance(name, question);
            }
        }
        return currentDistance;
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
