package org.example.client;

import org.example.common.Commands;

import java.io.*;
import java.net.Socket;

public class ClientUploader {

    /**
     * Отправляет .jar-файл с задачей на сервер по протоколу (через ObjectOutputStream).
     * Ждёт подтверждения загрузки.
     */
    public static void uploadJar(String serverAddr, byte[] jarBytes) {
        try {
            String[] hp = serverAddr.split(":");
            String host = hp[0];
            int port = Integer.parseInt(hp[1]);

            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in  = new ObjectInputStream(socket.getInputStream());

            // Команда
            out.writeObject(Commands.UPLOAD_JAR);
            // Байты jar
            out.writeObject(jarBytes);
            out.flush();

            // Читаем ответ
            Object response = in.readObject();
            System.out.println("Server " + serverAddr + " response: " + response);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
