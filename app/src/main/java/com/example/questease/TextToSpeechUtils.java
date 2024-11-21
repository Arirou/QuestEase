package com.example.questease;



import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TextToSpeechUtils {
    private static TextToSpeech textToSpeech;

    public static void initializeTextToSpeech(Context context, View rootView) {
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.FRANCE);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeechUtils", "Langue non supportée");
                } else {
                    // Récupérer et lire tous les textes
                    List<String> allTexts = getAllTextFromTextViews(rootView);
                    speakAllTexts(allTexts);
                }
            } else {
                Log.e("TextToSpeechUtils", "Initialisation échouée");
            }
        });
    }

    public static List<String> getAllTextFromTextViews(View rootView) {
        List<String> textList = new ArrayList<>();
        if (rootView instanceof TextView) {
            TextView textView = (TextView) rootView;
            String text = textView.getText().toString();
            if (!text.isEmpty()) {
                textList.add(text);
            }
        } else if (rootView instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) rootView;
            for (int i = 0; i < group.getChildCount(); i++) {
                textList.addAll(getAllTextFromTextViews(group.getChildAt(i)));
            }
        }
        return textList;
    }

    public static void speakAllTexts(List<String> texts) {
        for (String text : texts) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null, null);
        }
    }

    public static void releaseTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
