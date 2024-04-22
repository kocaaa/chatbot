package com.chatbot.chatbot.constants;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int NO_MINIMAL_LENGTH = 0;
    public static final int MINIMAL_NAME_LENGTH = 2;
    public static final int DEFAULT_DATE_LENGTH = 6;
    public static final String CHAT_BOT_ENDPOINT = "/question";
    public static final String SUBJECTS_ENDPOINT = "/all_subjects";
    public static final String EXAM_SCHEDULE_ENDPOINT = "/all_exam_schedules";
    public static final String MODULES_RESPONSE = "Na našem fakultetu postoji tri modula. Studenti imaju mogućnost da odaberu jedan od ta tri modula od druge godine studija. Više o tome možete pročitati <a href=\"https://imi.pmf.kg.ac.rs/informatika-studije\" target=\"_blank\">ovde</a>.";
    public static final String UNSUPPORTED_QUESTION = "Ne umem da odgovorim na ovo pitanje.";
    public static final String JANUARY = "Januar";
    public static final String FEBRUARY = "Februar";
    public static final String MARCH = "Mart";
    public static final String APRIL = "April";
    public static final String MAY = "Maj";
    public static final String JUNE = "Jun";
    public static final String JULY = "Jul";
    public static final String AUGUST = "Avgust";
    public static final String SEPTEMBER = "Septembar";
    public static final String OCTOBER = "Oktobar";
    public static final String NOVEMBER = "Novembar";
    public static final String DECEMBER = "Decembar";
    public static final String DIV_CUSTOM_PANEL = "div.custom-panel";
    public static final String ROW_SELECTOR = "div.row:not(div.row > div.row)";
    public static final String EMPLOYEE_URL = "https://imi.pmf.kg.ac.rs/nastavno-osoblje";
    public static final String MOODLE_URL = "https://imi.pmf.kg.ac.rs/moodle/";
    public static final String EXAMINATION_PERIODS_URL = "https://www.pmf.kg.ac.rs/?id=527";
    public static final String REGISTRATION_STRING = "ПРИЈАВЉИВАЊЕ ИСПИТА ЗА";
    public static final String EMPLOYEE_ROW_CLASS = "nastavnici_row";
    public static final String EMPLOYEE_CELL_NAME = "ime_nastavnika";
    public static final String EMPLOYEE_CELL_TITLE = "zvanje_nastavnika";
    public static final String EMPLOYEE_CELL_CABINET = "kabinet_nastavnika";
    public static final String EMPLOYEE_CELL_LOCALE = "lokal_nastavnika";
    public static final String EMPLOYEE_CELL_EMAIL = "mail_nastavnika";
    public static final String EMPLOYEE_CELL_CONSULTATION = "konsultacije_nastavnika";
    public static final String PARTIAL_LINK_YEAR = " godina";
    public static final String PARTIAL_LINK_SEMESTER = " semestar";
    public static final String HREF_TAG = "href";
    public static final List<String> OAS_LIST = Arrays.asList("OAS MATEMATIKE", "OAS INFORMATIKE");
    public static final List<String> MAS_LIST = Arrays.asList("MAS MATEMATIKE", "MAS INFORMATIKE");

    private Constants() {

    }
}
