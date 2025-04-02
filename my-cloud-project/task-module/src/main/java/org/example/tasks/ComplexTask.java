package org.example.tasks;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.example.common.RemoteExecutable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComplexTask {

    private static final Gson gson = new Gson();

    @RemoteExecutable
    public List<String> fetchAndParsePosts(List<Integer> postIds) {
        List<String> titles = new ArrayList<>();

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            for (Integer postId : postIds) {
                String url = "https://jsonplaceholder.typicode.com/posts/" + postId;
                HttpGet request = new HttpGet(url);
                String json = client.execute(request, response ->
                    EntityUtils.toString(response.getEntity()));

                JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
                String title = jsonObject.get("title").getAsString();
                titles.add("Post #" + postId + ": " + title);
            }
        } catch (IOException e) {
            throw new RuntimeException("HTTP Request failed: " + e.getMessage(), e);
        }

        return titles;
    }
}
