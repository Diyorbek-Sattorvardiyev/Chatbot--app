package com.example.chatbotapp;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.*;

public class GeminiTask extends AsyncTask<String, Void, String> {

    private final Context context;
    private final ResponseListener listener;


    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public interface ResponseListener {
        void onResponseReceived(String response);
    }

    public GeminiTask(Context context, ResponseListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String userMessage = params[0];
        String apiKey = context.getString(R.string.gemini_api_key); // res/values/strings.xml da

        OkHttpClient client = new OkHttpClient();

        try {
            JSONObject part = new JSONObject();
            part.put("text", userMessage);

            JSONObject content = new JSONObject();
            content.put("role", "user");
            content.put("parts", new JSONArray().put(part));

            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", new JSONArray().put(content));

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(API_URL + "?key=" + apiKey)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                JSONObject responseJson = new JSONObject(responseStr);
                JSONArray candidates = responseJson.getJSONArray("candidates");

                if (candidates.length() > 0) {
                    JSONObject contentObj = candidates.getJSONObject(0).getJSONObject("content");
                    JSONArray parts = contentObj.getJSONArray("parts");
                    return parts.getJSONObject(0).getString("text");
                } else {
                    return "Javob topilmadi.";
                }

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
