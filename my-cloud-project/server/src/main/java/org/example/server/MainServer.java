package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Сервер, слушает порт (по умолчанию 5000).
 * При подключении клиентов создаёт ServerHandler (в отдельном потоке).
 */
public class MainServer {
    public static void main(String[] args) {
        int port = 5000;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        System.out.println("Server starting on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("New connection: " + client.getRemoteSocketAddress());

                ServerHandler handler = new ServerHandler(client);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
