package org.example.server;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище загруженных .jar (динамическая загрузка).
 */
public class JarRepository {

    private static final JarRepository instance = new JarRepository();
    public static JarRepository getInstance() {
        return instance;
    }

    private final List<URLClassLoader> loaders = new ArrayList<>();

    private JarRepository() {}

    public void addJar(byte[] jarBytes) throws Exception {
        // Сохраняем во временный файл
        File tmp = File.createTempFile("uploaded-", ".jar");
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            fos.write(jarBytes);
        }

        // Создаём URLClassLoader
        URL url = tmp.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url}, this.getClass().getClassLoader());
        loaders.add(loader);

        System.out.println("Jar added: " + tmp.getAbsolutePath());
    }

    public Class<?> loadClass(String className) {
        // Проходим с конца
        for (int i = loaders.size() - 1; i >= 0; i--) {
            try {
                Class<?> c = loaders.get(i).loadClass(className);
                if (c != null) {
                    return c;
                }
            } catch (ClassNotFoundException e) {
                // ignore
            }
        }
        return null;
    }
}
