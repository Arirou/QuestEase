package com.example.questease.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.questease.R;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questease.R;
import com.example.questease.RotatingPictures;
import com.example.questease.RotatingPictures2;
import com.example.questease.Theme;
import com.example.questease.WebSocketService;

import android.media.MediaPlayer;

import org.json.JSONObject;

import java.util.HashSet;

public class ジャイロスコープアクティビティ extends Theme implements SensorEventListener {
    private WebSocketService webSocketサービス;
    private SensorManager センサーマネージャ;
    private Sensor 回転ベクトルセンサー;
    private boolean バインド済み = false;
    private boolean 作成済み = false;
    private ImageView ロック, 固定ロック;
    private MediaPlayer メディアプレイヤー;
    private Button 閉じるボタン, ルールボタン;
    private Dialog チュートリアルダイアログ;
    private TextView カードタイトル;
    private TextView カード内容;

    private boolean エラーポップアップ表示中 = false;
    private String ルールタイトル = "\n\n\"ゲームルール\"";
    private String ルール内容 = "\"スマートフォンを回して針を次の角度に揃えてください：45°、180°、300°。\"" +
            "                        \"すべてのコードが見つかると、ゲームクリアです。\"";


    private final float[] 正解 = {70f, 180f, 275f}; // 正解の角度
    private final float 許容範囲 = 3f;
    private HashSet<Float> 発見済み解答 = new HashSet<>();

    private float[] 回転行列 = new float[9];
    private float[] 方位角 = new float[3];
    private ServiceConnection 接続 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WebSocketService.LocalBinder binder = (WebSocketService.LocalBinder) service;
            webSocketサービス = binder.getService();
            バインド済み = true;
            if (webSocketサービス != null) {
                Log.e("テスト", "WebSocketサービス経由でメッセージを送信");
                webSocketサービス.sendMessage("requestLobbies", "こんにちは、みんな！");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            バインド済み = false;
        }
    };
    private BroadcastReceiver メッセージ受信機 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("メインアクティビティ", "ブロードキャストを受信");
            if (intent.getAction().equals("WebSocketMessage")) {
                String jsonメッセージ = intent.getStringExtra("message");
                Log.d("メインアクティビティ", "受信したメッセージ：" + jsonメッセージ);
                try {
                    JSONObject jsonObject = new JSONObject(jsonメッセージ);
                    String タグ = jsonObject.getString("tag");
                    String メッセージ = jsonObject.getString("message");
                    if ("WebSocketError".equals(タグ) && メッセージ.equals("WebSocket is not connected!")) {
                        if (!エラーポップアップ表示中) {
                            ViewGroup view = findViewById(R.id.main);
                            showServerErrorPopUp(view);
                            エラーポップアップ表示中 = true;
                        }
                    } else if ("successPopup".equals(タグ)) {
                        ViewGroup viewGroup = findViewById(R.id.main);
                        メディアプレイヤー = MediaPlayer.create(ジャイロスコープアクティビティ.this, R.raw.professor_layton_sucess);
                        メディアプレイヤー.start();
                        showTutorialPopup(
                                "おめでとうございます！",
                                "おめでとうございます！\n\n鍵を開けることができました。\n数秒後に次のコンテンツが表示されます。",
                                viewGroup
                        );
                    } else if ("startActivity".equals(タグ)) {
                        Log.d("ロビー", "アクティビティ開始メッセージ受信：" + メッセージ);
                        Intent intentgame = identifyActivity(メッセージ);
                        startActivity(intentgame);
                        finish();
                    }

                } catch (Exception e) {

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscopes);

        ロック = findViewById(R.id.lock);
        固定ロック = findViewById(R.id.solidLock);
        閉じるボタン = findViewById(R.id.closeButton);
        ルールボタン = findViewById(R.id.rulesButton);
        SharedPreferences 共有設定 = getSharedPreferences("QuestEasePrefs", MODE_PRIVATE);
        ApplyParameters(共有設定);

        センサーマネージャ = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        回転ベクトルセンサー = センサーマネージャ.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        ViewGroup レイアウト = findViewById(R.id.main);
        showTutorialPopup(this.ルールタイトル, this.ルール内容, レイアウト);

        if (回転ベクトルセンサー == null) {
            Toast.makeText(this, "回転ベクトルセンサーが利用できません", Toast.LENGTH_SHORT).show();
            finish();
        }

        閉じるボタン.setOnClickListener(v -> finish());
        ルールボタン.setOnClickListener(v -> showTutorialPopup(this.ルールタイトル, this.ルール内容, レイアウト));
        Intent サービスインテント = new Intent(this, WebSocketService.class);
        startService(サービスインテント);
        bindService(サービスインテント, 接続, Context.BIND_AUTO_CREATE);

        Log.d("メインアクティビティ", "ブロードキャストレシーバーを登録");
        IntentFilter フィルター = new IntentFilter("WebSocketMessage");
        registerReceiver(メッセージ受信機, フィルター, Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        センサーマネージャ.registerListener(this, 回転ベクトルセンサー, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        センサーマネージャ.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(回転行列, event.values);
            SensorManager.getOrientation(回転行列, 方位角);

            float 方位角度 = (float) Math.toDegrees(方位角[0]);
            方位角度 = (方位角度 + 360) % 360;

            ロック.setRotation(方位角度);
            checkForSolution(方位角度);
        }
    }

    private void checkForSolution(float 方位角度) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        
    }
}
