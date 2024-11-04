package com.example.questease;

import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Theme extends AppCompatActivity {
    public void ApplyParameters(SharedPreferences sharedPreferences){
        //Protanomalie = 1
        //Protanopie = 2
        //deuteranomalie = 3
        //deuteranopie = 4
        Log.d("SharedPreferences", "Valeur de daltonisme: " + sharedPreferences.getInt("daltonisme", 0));
        Log.d("SharedPreferences","je vais essayer d'appliquer un thème");
        if(sharedPreferences.getInt("daltonisme",0)== 1) {
            setTheme(R.style.Theme_Questease_Protanomalie);}
        else if(sharedPreferences.getInt("daltonisme",0)== 2){
            setTheme(R.style.Theme_Questease_Protanopie);
        } else if (sharedPreferences.getInt("daltonisme",0)==3) {
            setTheme(R.style.Theme_Questease_Deuteranomalie);
        } else if (sharedPreferences.getInt("daltonisme",0) == 4) {
            setTheme(R.style.Theme_Questease_deuteranopie);
        }
    }


    public void adjustTextSize(List<View> views) {
        // Convertir la taille de texte souhaitée en pixels pour la comparaison
        float textSizeLargePx = getResources().getDimension(R.dimen.text_size_large);

        for (View view : views) {
            float textSize14spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, this.getResources().getDisplayMetrics());
            float textSize19spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 19, this.getResources().getDisplayMetrics());
            float textSize24spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, this.getResources().getDisplayMetrics());
            float textSize31spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 31, this.getResources().getDisplayMetrics());
            float textSize35spInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 35, this.getResources().getDisplayMetrics());
            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                if (textView.getTextSize() == textSize14spInPx) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize19spInPx);
                } else if (textView.getTextSize() == textSize19spInPx) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize24spInPx);
                } else if (textView.getTextSize() == textSize24spInPx) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize31spInPx);
                }
                else if (textView.getTextSize() == textSize31spInPx) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize35spInPx);
                }
            } else if (view instanceof Button) {
                Button button = (Button) view;
                if (button.getTextSize() == textSize14spInPx) {
                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize19spInPx);
                } else if(button.getTextSize()==textSize19spInPx) {
                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize24spInPx);
                } else if(button.getTextSize()==textSize24spInPx) {
                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize31spInPx);
                } else if(button.getTextSize()==textSize31spInPx) {
                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize35spInPx);
                }
            }
            else if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.getTextSize() == textSize14spInPx) {
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize19spInPx);
                } else if (checkBox.getTextSize() == textSize19spInPx) {
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize24spInPx);
                } else if (checkBox.getTextSize() == textSize24spInPx) {
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize31spInPx);
                } else if (checkBox.getTextSize() == textSize31spInPx) {
                    checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize35spInPx);
                }
            }
        }
    }

}
