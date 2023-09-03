package com.chatbot.chatbot.utils;

public class LatinUtil {
    public static String convertToLowerAlphabet(String string) {
        if (!string.isBlank()) {
            string = string.toLowerCase().replace("ć", "c")
                    .replace("č", "c")
                    .replace("š", "s")
                    .replace("đ", "dj")
                    .replace("ž", "z");
        }

        return string;
    }
}
