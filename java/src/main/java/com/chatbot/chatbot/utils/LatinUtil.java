package com.chatbot.chatbot.utils;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

public class LatinUtil {
    private LatinUtil() {

    }

    public static String convertToLowerAlphabet(String string) {
        if (isNotBlank(string)) {
            string = string.toLowerCase().replace("ć", "c")
                    .replace("č", "c")
                    .replace("š", "s")
                    .replace("đ", "dj")
                    .replace("ž", "z");
        } else {
            string = "";
        }

        return string;
    }
}
