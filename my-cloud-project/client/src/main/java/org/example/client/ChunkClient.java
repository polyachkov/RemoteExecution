package org.example.client;

import org.example.common.Commands;
import org.example.common.RemoteRequest;
import org.example.common.RemoteResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChunkClient {

    /**
     * Делит входной список data на чанки по количеству серверов.
     * Отправляет каждый чанк на соответствующий сервер.
     * Запрашивает выполнение метода className.methodName(chunk) на сервере.
     * Собирает и возвращает все частичные результаты.
     */
    public static <T, R> List<R> distributedMap(
            List<T> data,
            List<String> servers,
            String className,
            String methodName
    ) {
        int chunkSize = (int) Math.ceil((double) data.size() / servers.size());
        List<List<T>> chunks = splitIntoChunks(data, chunkSize);

        List<R> allResults = new ArrayList<>();
        for (int i = 0; i < servers.size(); i++) {
            if (i < chunks.size()) {
                List<T> chunk = chunks.get(i);
                String server = servers.get(i);

                List<R> partial = remoteMapInvoke(server, chunk, className, methodName);
                allResults.addAll(partial);
            }
        }
        return allResults;
    }

    @SuppressWarnings("unchecked")
    private static <T, R> List<R> remoteMapInvoke(String serverAddr, List<T> chunk, String className, String methodName) {
        try (
            Socket socket = new Socket(serverAddr.split(":")[0], Integer.parseInt(serverAddr.split(":")[1]));
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            out.writeObject(Commands.INVOKE_METHOD);

            RemoteRequest req = new RemoteRequest(className, methodName, new Object[]{chunk});
            out.writeObject(req);
            out.flush();

            RemoteResponse resp = (RemoteResponse) in.readObject();
            if (resp.isSuccess()) {
                return (List<R>) resp.getResult();
            } else {
                throw new RuntimeException("Server error: " + resp.getErrorMessage());
            }

        } catch (Exception e) {
            throw new RuntimeException("remoteMapInvoke fail", e);
        }
    }

    private static <T> List<List<T>> splitIntoChunks(List<T> data, int chunkSize) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i += chunkSize) {
            result.add(new ArrayList<>(data.subList(i, Math.min(data.size(), i + chunkSize))));
        }
        return result;
    }
}
