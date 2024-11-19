package com.example.questease;

import android.speech.tts.TextToSpeech;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import java.util.Locale;

public class TextToSpeechHelper implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private Context context;
    boolean isTtsInitialized;

    public TextToSpeechHelper(Context context) {
        this.context = context;
        // Initialiser TextToSpeech
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Configurer la langue de TextToSpeech
            int langResult = textToSpeech.setLanguage(Locale.FRENCH);
            if (langResult == TextToSpeech.LANG_MISSING_DATA
                    || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Gérer le cas où la langue n'est pas supportée
                Log.e("TTS", "Langue non supportée ou donnée manquante");
            } else {
                // Le moteur TTS est prêt, on peut commencer à parler
                Log.d("TTS", "Moteur TTS prêt");
                boolean isTtsInitialized = true;
            }
        } else {
            // Si l'initialisation échoue
            Log.e("TTS", "Échec de l'initialisation du moteur TTS");
        }
    }





    public boolean lireTextViews(LinearLayout layout) {

        // Parcourir tous les enfants du layout
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);

            // Vérifier si c'est une TextView
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                String text = textView.getText().toString();
                Log.d("texte a lire", text);

                // Lire le texte avec TextToSpeech
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

            }
        }
        return true;
    }


    public void stop() {
        // Libérer les ressources de TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
