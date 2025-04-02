package org.example.tasks;

import org.example.common.RemoteExecutable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.stream.Collectors;

public class WebPageAnalyzerTask {

    @RemoteExecutable
    public Map<String, Integer> analyzePage(String url) {
        try {
            // Скачиваем контент страницы
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text();

            // Считаем частоту слов
            String[] words = text.toLowerCase().replaceAll("[^a-zа-яё0-9]", " ").split("\\s+");
            Map<String, Integer> freq = new HashMap<>();
            for (String word : words) {
                if (word.length() > 3) { // игнорируем короткие слова
                    freq.put(word, freq.getOrDefault(word, 0) + 1);
                }
            }

            // Возвращаем ТОП-5 слов
            return freq.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new));

        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze URL " + url + ": " + e.getMessage(), e);
        }
    }
}
