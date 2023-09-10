package com.chatbot.chatbot.utils;

public class LevenshteinUtil {
    private LevenshteinUtil() {

    }

    public static Integer findMostSimilarDistance(String target, String sentence) {
        String[] words = sentence.split("\\s+");
        int minDistance = Integer.MAX_VALUE;

        for (String word : words) {
            int distance = calculateLevenshteinDistance(target, word);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    public static int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
