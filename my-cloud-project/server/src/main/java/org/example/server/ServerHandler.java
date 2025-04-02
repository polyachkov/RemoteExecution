package org.example.server;

import org.example.common.Commands;
import org.example.common.RemoteRequest;
import org.example.common.RemoteResponse;
import org.example.common.RemoteExecutable;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler implements Runnable {

    private final Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            // Считываем команду (UPLOAD_JAR / INVOKE_METHOD)
            Commands cmd = (Commands) in.readObject();
            System.out.println("Получена команда: " + cmd);

            switch (cmd) {
                case UPLOAD_JAR:
                    handleUploadJar(in, out);
                    break;
                case INVOKE_METHOD:
                    handleInvokeMethod(in, out);
                    break;
                default:
                    out.writeObject("Unknown command: " + cmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignore) {}
        }
    }

    private void handleUploadJar(ObjectInputStream in, ObjectOutputStream out) throws Exception {
        // Читаем байты jar
        byte[] jarBytes = (byte[]) in.readObject();

        // Добавляем в JarRepository
        JarRepository.getInstance().addJar(jarBytes);

        out.writeObject("JAR uploaded successfully");
        out.flush();

        System.out.println("JAR загружен, size = " + jarBytes.length + " bytes");
    }

    private void handleInvokeMethod(ObjectInputStream in, ObjectOutputStream out) throws Exception {
        // Читаем RemoteRequest
        RemoteRequest request = (RemoteRequest) in.readObject();
        System.out.println("INVOKE: " + request);

        // Загружаем класс из ранее загруженного jar
        Class<?> clazz = JarRepository.getInstance().loadClass(request.getClassName());
        if (clazz == null) {
            out.writeObject(new RemoteResponse(false, null, "Class not found: " + request.getClassName()));
            return;
        }

        // Находим метод по имени и проверке @RemoteExecutable
        Method method = findMethod(clazz, request.getMethodName(), request.getArgs());
        if (method == null) {
            out.writeObject(new RemoteResponse(false, null, "Method not found or not annotated"));
            return;
        }

        // Создаём экземпляр класса
        Object instance = clazz.getDeclaredConstructor().newInstance();

        // Поддержка distributedMap:
        // Если метод принимает ровно один аргумент типа List<?>,
        // и в RemoteRequest передаётся аргумент List<T>,
        // то выполняем его в режиме map (каждый элемент отдельно)
        Object result;
        try {
            if (method.getParameterCount() == 1 && request.getArgs()[0] instanceof List) {
                List<?> inputList = (List<?>) request.getArgs()[0];
                List<Object> mappedResults = new ArrayList<>();

                // Вызываем метод отдельно для каждого элемента списка
                for (Object item : inputList) {
                    Object singleResult = method.invoke(instance, item);
                    mappedResults.add(singleResult);
                }
                result = mappedResults;
            } else {
                // Обычный вызов метода (не map)
                result = method.invoke(instance, request.getArgs());
            }
        } catch (Exception e) {
            out.writeObject(new RemoteResponse(false, null, "Invocation error: " + e.getMessage()));
            return;
        }

        out.writeObject(new RemoteResponse(true, result, null));
        out.flush();
    }


    /**
     * Находим метод по имени, числу аргументов, и проверяем что @RemoteExecutable стоит.
     */
    private Method findMethod(Class<?> clazz, String methodName, Object[] args) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (!m.isAnnotationPresent(RemoteExecutable.class)) {
                continue;
            }
            if (m.getName().equals(methodName)
                    && m.getParameterCount() == args.length) {
                return m;
            }
        }
        return null;
    }
}
