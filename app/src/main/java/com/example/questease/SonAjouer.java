package com.example.questease;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.Locale;

public class SonAjouer implements OnInitListener {

    private TextToSpeech textToSpeech;
    private Context context;

    public SonAjouer(Context context) {
        this.context = context;
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Définir la langue sur l'anglais ou une autre langue prise en charge
            int langResult = textToSpeech.setLanguage(Locale.FRENCH);
            if (langResult == TextToSpeech.LANG_MISSING_DATA
                    || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("SonAjouer", "Langue non supportée ou données manquantes");
            } else {
                Log.d("SonAjouer", "Initialisation réussie de SonAjouer");
            }
        } else {
            Log.e("SonAjouer", "Échec de l'initialisation");
        }
    }

    public void jouer(String texte) {
        if (textToSpeech != null) {
            textToSpeech.speak(texte, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void arreter() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public void fermer() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}
