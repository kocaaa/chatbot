package com.chatbot.chatbot.utils;

import java.util.HashMap;
import java.util.Map;

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

    public static String convertCyrillicToLatin(String cyrillicText) {
        Map<Character, String> swap = new HashMap<>();
        swap.put('А', "A");
        swap.put('а', "a");
        swap.put('Б', "B");
        swap.put('б', "b");
        swap.put('В', "V");
        swap.put('в', "v");
        swap.put('Г', "G");
        swap.put('г', "g");
        swap.put('Д', "D");
        swap.put('д', "d");
        swap.put('Ђ', "Đ");
        swap.put('ђ', "đ");
        swap.put('Е', "E");
        swap.put('е', "e");
        swap.put('Ж', "Ž");
        swap.put('ж', "ž");
        swap.put('З', "Z");
        swap.put('з', "z");
        swap.put('И', "I");
        swap.put('и', "i");
        swap.put('Ј', "J");
        swap.put('ј', "j");
        swap.put('К', "K");
        swap.put('к', "k");
        swap.put('Л', "L");
        swap.put('л', "l");
        swap.put('Љ', "Lj");
        swap.put('љ', "lj");
        swap.put('М', "M");
        swap.put('м', "m");
        swap.put('Н', "N");
        swap.put('н', "n");
        swap.put('Њ', "Nj");
        swap.put('њ', "nj");
        swap.put('О', "O");
        swap.put('о', "o");
        swap.put('П', "P");
        swap.put('п', "p");
        swap.put('Р', "R");
        swap.put('р', "r");
        swap.put('С', "S");
        swap.put('с', "s");
        swap.put('Т', "T");
        swap.put('т', "t");
        swap.put('Ћ', "Ć");
        swap.put('ћ', "ć");
        swap.put('У', "U");
        swap.put('у', "u");
        swap.put('Ф', "F");
        swap.put('ф', "f");
        swap.put('Х', "H");
        swap.put('х', "h");
        swap.put('Ц', "C");
        swap.put('ц', "c");
        swap.put('Ч', "Č");
        swap.put('ч', "č");
        swap.put('Џ', "Dž");
        swap.put('џ', "dž");
        swap.put('Ш', "Š");
        swap.put('ш', "š");
        swap.put('-', " ");

        StringBuilder latinTextBuilder = new StringBuilder();
        for (char c : cyrillicText.toCharArray()) {
            // Convert Cyrillic characters to Latin directly
            if (swap.containsKey(c)) {
                latinTextBuilder.append(swap.get(c));
            } else {
                latinTextBuilder.append(c);
            }
        }
        return latinTextBuilder.toString();
    }
}
