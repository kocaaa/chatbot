package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.enums.Question;
import com.chatbot.chatbot.models.*;
import com.chatbot.chatbot.repositories.EmployeeDao;
import com.chatbot.chatbot.repositories.ExamRegistrationDao;
import com.chatbot.chatbot.services.PythonService;
import com.chatbot.chatbot.services.QuestionService;
import com.chatbot.chatbot.utils.LatinUtil;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static com.chatbot.chatbot.constants.Constants.*;
import static com.chatbot.chatbot.utils.LevenshteinUtil.findMostSimilarDistance;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final PythonService pythonService;
    private final EmployeeDao employeeDao;
    private final ExamRegistrationDao examRegistrationDao;

    public QuestionServiceImpl(PythonService pythonService, EmployeeDao employeeDao, ExamRegistrationDao examRegistrationDao) {
        this.pythonService = pythonService;
        this.employeeDao = employeeDao;
        this.examRegistrationDao = examRegistrationDao;
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

    @Override
    public String processResponse(PyResponse response, PyMessage message) throws JSONException {
        return processQuestion(response.getQuestion(), message.getQuestion());
    }

    private String processQuestion(Question question, String message) throws JSONException {
        message = message.toLowerCase();
        return switch (question) {
            case PROFESSOR_SUBJECT -> processProfessorSubject(message);
            case ASSISTANT_SUBJECT -> processAssistantSubject(message);
            case CONSULTATIONS -> processConsultations(message);
            case CONTACT_EMAIL -> predictEmail(message);
            case OFFICE_LOCATION -> processOfficeLocation(message);
            case EXAM_SCHEDULE -> predictExamSchedule(message);
            case EXAM_REGISTRATION -> processExamRegistration(message);
            case MODULES -> processModules();
            default -> processUnsupportedMessage();
        };
    }

    private String processModules() {
        return null;
    }

    private String predictExamSchedule(String question) throws JSONException {
        YearExams predictedYear = null;
        Exam predictedExam = null;
        int minDistance = Integer.MAX_VALUE;

        List<YearExams> schedule = pythonService.getExamSchedule();

        for (YearExams yearExam : schedule) {
            for (Exam exam : yearExam.getExams()) {
                int currentDistance = calculateCurrentDistance(question, exam.getName(), NO_MINIMAL_LENGTH);
                int initialDistance = calculateCurrentDistance(question, exam.getInitials(), NO_MINIMAL_LENGTH);

                if (currentDistance < minDistance || initialDistance == 0) {
                    predictedYear = yearExam;
                    predictedExam = exam;
                    minDistance = initialDistance == 0 ? initialDistance : currentDistance;
                }
            }
        }

        String predictedMonthName = predictMonth(question);
        String predictedMonthTime = null;
        int timeIndex = -1;

        if (predictedYear != null && predictedYear.getMonths() != null) {
            List<Month> months = predictedYear.getMonths();
            for (int i = 0; i < months.size(); i++) {
                Month month = months.get(i);
                if (month.getName().equals(predictedMonthName)) {
                    predictedMonthTime = month.getTime();
                    timeIndex = i;
                }
            }
        }

        String examDate = timeIndex >= 0 && timeIndex < predictedExam.getDates().size() ? predictedExam.getDates().get(timeIndex) : null;
        String responseMessage;

        if (predictedMonthName != null && examDate != null) {
            if (examDate.length() > DEFAULT_DATE_LENGTH) {
                responseMessage = "Ispit ce biti odrzan " + examDate;
            } else {
                responseMessage = "Ispit ce biti odrzan " + examDate + " u " + predictedMonthTime;
            }
        } else {
            responseMessage = "Tekst pitanja mora da sadrzi rok u kojem se ispit odrzava (jun, junski..)";
        }

        return responseMessage;
    }

    public String predictMonth(String question) {
        List<String> june = Arrays.asList("jun", "junu", "junski", "junskom");
        List<String> july = Arrays.asList("jul", "julu", "julski", "julskom");
        List<String> august = Arrays.asList("avgust", "avgustu", "avgustovski", "avgustovskom");
        List<String> september = Arrays.asList("septembar", "septembru", "septembarski", "septembarskom");

        String month;

        month = checkMonth(question, june, JUNE) != null ? checkMonth(question, june, JUNE) : null;
        month = checkMonth(question, july, JULY) != null ? checkMonth(question, july, JULY) : month;
        month = checkMonth(question, august, AUGUST) != null ? checkMonth(question, august, AUGUST) : month;
        month = checkMonth(question, september, SEPTEMBER) != null ? checkMonth(question, september, SEPTEMBER) : month;

        return month;
    }

    public String extendedPredictMonth(String question) {
        String month = predictMonth(question);

        if (month != null) {
            return month;
        }

        List<String> january = Arrays.asList("januar", "januaru", "januarski", "januarskom");
        List<String> february = Arrays.asList("februar", "februaru", "februarski", "februarskom");
        List<String> march = Arrays.asList("mart", "martu", "martovski", "martovskom");
        List<String> april = Arrays.asList("april", "aprilu", "aprilsli", "aprilskom");
        List<String> may = Arrays.asList("maj", "maju", "majski", "majskom");
        List<String> october = Arrays.asList("oktobar", "oktobru", "oktobarski", "oktobarskom");
        List<String> november = Arrays.asList("novembar", "novembru", "novembarski", "novembarskom");
        List<String> december = Arrays.asList("decembar", "decembru", "decembarski", "decembarskom");

        month = checkMonth(question, january, JANUARY) != null ? checkMonth(question, january, JANUARY) : null;
        month = checkMonth(question, february, FEBRUARY) != null ? checkMonth(question, february, FEBRUARY) : month;
        month = checkMonth(question, march, MARCH) != null ? checkMonth(question, march, MARCH) : month;
        month = checkMonth(question, april, APRIL) != null ? checkMonth(question, april, APRIL) : month;
        month = checkMonth(question, may, MAY) != null ? checkMonth(question, may, MAY) : month;
        month = checkMonth(question, october, OCTOBER) != null ? checkMonth(question, october, OCTOBER) : month;
        month = checkMonth(question, november, NOVEMBER) != null ? checkMonth(question, november, NOVEMBER) : month;
        month = checkMonth(question, december, DECEMBER) != null ? checkMonth(question, december, DECEMBER) : month;

        return month;
    }

    private String checkMonth(String question, List<String> month, String response) {
        for (String word : month) {
            if (calculateCurrentDistance(question, word, NO_MINIMAL_LENGTH) == 0) {
                return response;
            }
        }
        return null;
    }

    private String processExamRegistration(String question) {
        String month = extendedPredictMonth(question);

        String response = "Tekst pitanja mora da sadrzi rok u kojem se ispit odrzava (jun, junski..)";

        if (month != null) {
            ExamRegistration examRegistration = examRegistrationDao.findByMonthContaining(month);
            if (examRegistration != null) {
                response = MessageFormat.format("Rok za prijavljivanje za {0} rok je {1}", examRegistration.getMonth(), examRegistration.getDate());
            }
        }

        return response;
    }

    private String processProfessorSubject(String question) throws JSONException {
        Subject predictedSubject = predictSubject(question);
        String predictedProfessor;

        if (predictedSubject != null) {
            predictedProfessor = "Predmet " + predictedSubject.getName() + " predaje " + predictedSubject.getProfessor();
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
            predictedAssistants = "Asistenti na " + predictedSubject.getName() + " su " + String.join(", ", predictedSubject.getAssistants());
        } else {
            predictedAssistants = "Nisam uspeo da pronadjem asistente na predmetu.";
        }

        return predictedAssistants;
    }

    private String processConsultations(String question) {
        Employee predictedEmployee = predictEmployee(question);
        String predictedConsultations;

        if (predictedEmployee != null && !predictedEmployee.getConsultation().isBlank() && !predictedEmployee.getName().isBlank()) {
            predictedConsultations = predictedEmployee.getName() + " drzi konsultacije u sledecim terminima: " + predictedEmployee.getConsultation();
        } else if (predictedEmployee != null && predictedEmployee.getName() != null) {
            predictedConsultations = "Nisam uspeo da pronadjem termin konsultacija kod " + predictedEmployee.getName();
        } else {
            predictedConsultations = "Nisam uspeo da pronadjem validan termin konsultacija.";
        }

        return predictedConsultations;
    }

    private String predictEmail(String question) {
        Employee predictedEmployee = predictEmployee(question);
        String predictedEmail;

        if (predictedEmployee != null && !predictedEmployee.getEmail().isBlank() && !predictedEmployee.getName().isBlank()) {
            predictedEmail = "Email od " + predictedEmployee.getName() + " je " + predictedEmployee.getEmail();
        } else if (predictedEmployee != null && predictedEmployee.getName() != null) {
            predictedEmail = "Nisam uspeo da pronadjem email od " + predictedEmployee.getName() + ".";
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

    private String processOfficeLocation(String question) {
        Employee predictedEmployee = predictEmployee(question);
        String predictedOfficeLocation;

        if (predictedEmployee != null && !predictedEmployee.getCabinet().isBlank() && !predictedEmployee.getName().isBlank()) {
            predictedOfficeLocation = predictedEmployee.getName() + " mozete naći u kabinetu " + predictedEmployee.getCabinet();
        } else if (predictedEmployee != null && predictedEmployee.getName() != null) {
            predictedOfficeLocation = "Nisam uspeo da pronadjem kabinet od " + predictedEmployee.getName() + ".";
        } else {
            predictedOfficeLocation = "Nisam uspeo da pronadjem validan kabinet.";
        }

        return predictedOfficeLocation;
    }

    private String processUnsupportedMessage() {
        return UNSUPPORTED_QUESTION;
    }
}
