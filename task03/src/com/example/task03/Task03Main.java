package com.example.task03;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class Task03Main {

    public static void main(String[] args) throws IOException {
        List<Set<String>> anagrams = findAnagrams(new FileInputStream("task03/resources/singular.txt"), Charset.forName("windows-1251"));
        for (Set<String> anagram : anagrams) {
            System.out.println(anagram);
        }
    }

    public static List<Set<String>> findAnagrams(InputStream inputStream, Charset charset) {
        Set<String> words = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (isValidWord(word)) {
                    words.add(word);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading input stream", e);
        }

        Map<String, Set<String>> anagramGroups = new HashMap<>();

        for (String word : words) {
            String key = getAnagramKey(word);
            anagramGroups.computeIfAbsent(key, k -> new TreeSet<>()).add(word);
        }

        List<Set<String>> result = new ArrayList<>();
        for (Set<String> group : anagramGroups.values()) {
            if (group.size() >= 2) {
                result.add(group);
            }
        }

        result.sort(Comparator.comparing(group -> group.iterator().next()));
        return result;
    }

    private static boolean isValidWord(String word) {
        if (word.length() < 3) {
            return false;
        }
        return word.chars().allMatch(ch -> isRussianLetter((char) ch));
    }

    private static boolean isRussianLetter(char ch) {
        return (ch >= 'а' && ch <= 'я') || ch == 'ё';
    }

    private static String getAnagramKey(String word) {
        char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }
}