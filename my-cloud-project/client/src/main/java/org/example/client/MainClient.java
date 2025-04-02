package org.example.client;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainClient {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: MainClient <task-module.jar> <serverList>");
            System.exit(1);
        }

        String jarPath = args[0];
        List<String> servers = Arrays.asList(args[1].split(";"));

        byte[] jarBytes = Files.readAllBytes(Paths.get(jarPath));
        servers.forEach(srv -> ClientUploader.uploadJar(srv, jarBytes));

        // Список URL для анализа
        List<String> urls = Arrays.asList(
            "https://en.wikipedia.org/wiki/Java_(programming_language)",
            "https://en.wikipedia.org/wiki/Cloud_computing",
            "https://en.wikipedia.org/wiki/Distributed_computing",
            "https://en.wikipedia.org/wiki/Artificial_intelligence",
            "https://en.wikipedia.org/wiki/Big_data",
            "https://en.wikipedia.org/wiki/Machine_learning"
        );

        // Запускаем распределённый анализ
        List<Map<String, Integer>> results = ChunkClient.distributedMap(
            urls, servers,
            "org.example.tasks.WebPageAnalyzerTask", "analyzePage"
        );

        // Выводим результаты
        for (int i = 0; i < urls.size(); i++) {
            System.out.println("ТОП-5 слов для страницы " + urls.get(i));
            results.get(i).forEach((word, count) -> System.out.println(word + ": " + count));
            System.out.println("-----------------------------------");
        }
    }
}
