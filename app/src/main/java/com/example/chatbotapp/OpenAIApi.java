package com.example.chatbotapp;

import okhttp3.*;
import com.google.gson.*;

import java.io.IOException;

public class OpenAIApi {
    private static final String API_KEY = "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"; // 🔴 API KALITINGIZNI QO'YING!
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String getChatGPTResponse(String userMessage) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String json = "{"
                + "\"model\": \"gpt-3.5-turbo\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + userMessage + "\"}],"
                + "\"temperature\": 0.7"
                + "}";

        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("API xatosi: " + response);
            return new Gson().fromJson(response.body().string(), JsonObject.class)
                    .getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
}
