package com.chatbot.chatbot.enums;

import java.util.Locale;

public enum Question {
    EXAM_REGISTRATION("exam_registration"),
    PROFESSOR_SUBJECT("professor_subject"),
    ASSISTANT_SUBJECT("assistant_subject"),
    CONSULTATIONS("consultations"),
    CONTACT_EMAIL("contact_email"),
    OFFICE_LOCATION("office_location"),
    UNSUPPORTED_QUESTION("unsupported_question");

    private final String action;

    Question(String action) {
        this.action = action;
    }

    public static Question parse(String action) {
        return switch (action.toLowerCase(Locale.ROOT)) {
            case "exam_registration" -> EXAM_REGISTRATION;
            case "professor_subject" -> PROFESSOR_SUBJECT;
            case "assistant_subject" -> ASSISTANT_SUBJECT;
            case "consultations" -> CONSULTATIONS;
            case "contact_email" -> CONTACT_EMAIL;
            case "office_location" -> OFFICE_LOCATION;
            default -> UNSUPPORTED_QUESTION;
        };
    }
}
