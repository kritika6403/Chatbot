package com.kritika.chatbot;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
  EditText SpeakNow;
  ImageView mic;
  RecyclerView chatsRV;
  ArrayList<Modal> modalArrayList;
  SpeechRecognizer speechRecognizer;
    private static  final int RecordAudioCodeRequest=1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
      checkPermission();
 }
     SpeakNow = findViewById(R.id.idEdtMessage);
      mic = findViewById(R.id.idIBSend);
      chatsRV= findViewById(R.id.idRVChats);
      speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
      final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
      speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
      speechRecognizer.setRecognitionListener(new RecognitionListener() {
          @Override
          public void onReadyForSpeech(Bundle params) {

          }

          @Override
          public void onBeginningOfSpeech() {
              SpeakNow.setText("");
              SpeakNow.setHint("Listening...");
          }

          @Override
          public void onRmsChanged(float rms) {

          }

          @Override
          public void onBufferReceived(byte[] buffer) {
          }

          @Override
          public void onEndOfSpeech() {
          }

          @Override
          public void onError(int error) {
          }

          @Override
          public void onResults(Bundle results) {
              mic.setImageResource(R.drawable.ic_baseline_mic_off_24);
              ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
              SpeakNow.setText(data.get(0));
          }

          @Override
          public void onPartialResults(Bundle partialResults) {
          }

          @Override
          public void onEvent(int eventType, Bundle params) {
          }
      });
      mic.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              if (event.getAction() == MotionEvent.ACTION_UP){
                  speechRecognizer.stopListening();
              }
              if (event.getAction() == MotionEvent.ACTION_DOWN){
                  mic.setImageResource(R.drawable.ic_baseline_mic_24);
                  speechRecognizer.startListening(speechRecognizerIntent);
              }

              return false;
          }
      });
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
        ActivityCompat.requestPermissions(this,new String[]{RECORD_AUDIO},RecordAudioCodeRequest);}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioCodeRequest && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }
}