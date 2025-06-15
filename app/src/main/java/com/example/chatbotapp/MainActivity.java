package com.example.chatbotapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> chatMessages;
    private EditText messageInput;
    private TextView typingIndicator;
    private ImageView sendButton, micButton;

    private Handler typingHandler = new Handler();
    private int dotCount = 0;
    private boolean isTyping = false;

    private TextToSpeech textToSpeech;
    private static final int VOICE_INPUT_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.list_item);
        messageInput = findViewById(R.id.tvMessega);
        sendButton = findViewById(R.id.senMesseg);
        micButton = findViewById(R.id.voiceInput);
        typingIndicator = findViewById(R.id.typing_indicator);


        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages, message -> {
            speakUzbekText(message.getMessage()); 
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);


        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("uz"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });


        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                chatMessages.add(new Message("Siz", message, true));
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                recyclerView.scrollToPosition(chatMessages.size() - 1);
                messageInput.setText("");

                startTypingAnimation();

                new GeminiTask(MainActivity.this, response -> {
                    stopTypingAnimation();

                    chatMessages.add(new Message("Gemini", response, false));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerView.scrollToPosition(chatMessages.size() - 1);


                }).execute(message);

            }
        });


        micButton.setOnClickListener(v -> startVoiceInput());
    }


    private void speakUzbekText(String text) {
        if (textToSpeech != null) {
            textToSpeech.setLanguage(new Locale("uz"));
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }


    private void startTypingAnimation() {
        isTyping = true;
        typingIndicator.setVisibility(View.VISIBLE);
        typingIndicator.setText("Yozmoqda");

        typingHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isTyping) return;
                dotCount = (dotCount + 1) % 4;
                StringBuilder dots = new StringBuilder();
                for (int i = 0; i < dotCount; i++) dots.append(".");
                typingIndicator.setText("Yozmoqda" + dots);
                typingHandler.postDelayed(this, 500);
            }
        }, 500);
    }


    private void stopTypingAnimation() {
        isTyping = false;
        typingHandler.removeCallbacksAndMessages(null);
        typingIndicator.setVisibility(View.GONE);
    }


    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "uz-UZ");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Gapiring...");
        try {
            startActivityForResult(intent, VOICE_INPUT_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Ovozli kiritish mavjud emas", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_INPUT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                messageInput.setText(result.get(0));
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
