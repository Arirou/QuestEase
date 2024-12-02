package com.example.questease;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Sincere_Menteur extends AppCompatActivity {

    private int id;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private SincereMenteurApi sincereMenteurApi;
    private WebSocketService webSocketService;
    private boolean isBound = false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketService = binder.getService();
            isBound = true;
            Log.d("SearchLobby", "Service connected");
            webSocketService.sendMessage("requestLobbies", "salut à tous c'est fanta de searchlobby");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("WebSocketMessage")) {
                String jsonMessage = intent.getStringExtra("message");

                Log.d("SearchLobby", "Message reçu brut : " + jsonMessage);

                try {
                    // Analyser le message JSON
                    JSONObject jsonObject = new JSONObject(jsonMessage);
                    String tag = jsonObject.getString("tag");
                    String message = jsonObject.getString("message");

                    // Vous pouvez maintenant traiter la liste contenue dans le message JSON
                    JSONArray messageArray = new JSONArray(message);
                    List<String> lobbies = new ArrayList<>();
                    for (int i = 0; i < messageArray.length(); i++) {
                        lobbies.add(messageArray.getString(i));
                    }

                    Log.d("SearchLobby", "Tag: " + tag);
                    Log.d("SearchLobby", "Lobbies: " + lobbies);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sincere_menteur);

        id = 2;
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Initialiser Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://<IP_DU_SERVEUR>:<PORT>")  // Remplace avec l'IP et le port du serveur
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sincereMenteurApi = retrofit.create(SincereMenteurApi.class);

        if (id == 2) {
            partie2enigme();
        }

        Button buttonValider = findViewById(R.id.button5);
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifierReponses();
            }
        });
    }

    private void verifierReponses() {
        // Récupérer les choix des joueurs
        RadioGroup radioGroup1 = findViewById(R.id.radioGroup);
        int selectedId1 = radioGroup1.getCheckedRadioButtonId();

        RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
        int selectedId2 = radioGroup2.getCheckedRadioButtonId();

        boolean r1Sincere = (selectedId1 == R.id.radioButtonSincere);
        boolean r2Sincere = (selectedId2 == R.id.radioButtonSincere2);

        // Enregistrer les réponses dans la base de données locale
        dbHelper.addReponseSM(db, id, r1Sincere, r2Sincere);

        // Créer une Map pour envoyer les données
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("reponse1", r1Sincere);
        data.put("reponse2", r2Sincere);

        // Envoyer les données au serveur
        envoyerReponses(data);
        webSocketService.sendMessage("messageTest","ceci est un test, tuer moi");
    }

    private void envoyerReponses(Map<String, Object> data) {
        Call<Void> call = sincereMenteurApi.sendData(data);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Sincere_Menteur.this, "Réponses envoyées avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Sincere_Menteur.this, "Erreur lors de l'envoi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Sincere_Menteur.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void partie2enigme() {
        TextView tv1 = findViewById(R.id.textView4);
        TextView tv2 = findViewById(R.id.textView5);
        TextView tv3 = findViewById(R.id.textView6);
        TextView tv4 = findViewById(R.id.textView7);

        tv1.setText("Henri");
        tv2.setText("Jeanne");
        tv3.setText("Marie ment, mais je ne sais pas pour Jacque");
        tv4.setText("Jacque est sincère et Marie ment");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}






