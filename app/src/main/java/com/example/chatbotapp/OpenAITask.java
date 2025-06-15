package com.example.chatbotapp;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.*;

public class OpenAITask extends AsyncTask<String, Void, String> {
    private final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final Context context;
    private final ResponseListener listener;

    public interface ResponseListener {
        void onResponseReceived(String response);
    }

    public OpenAITask(Context context, ResponseListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String userMessage = params[0];
        String apiKey = context.getString(R.string.openai_api_key);
        OkHttpClient client = new OkHttpClient();

        try {
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", userMessage);

            JSONArray messagesArray = new JSONArray();
            messagesArray.put(message);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", messagesArray);
            requestBody.put("temperature", 0.7);

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseData);
                JSONArray choices = jsonResponse.getJSONArray("choices");
                return choices.getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            } else {
                return "Xatolik: " + response.code();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Xatolik yuz berdi: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onResponseReceived(result);
    }
}
