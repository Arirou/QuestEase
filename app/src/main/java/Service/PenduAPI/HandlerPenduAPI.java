package Service.PenduAPI;

import android.content.Context;
import android.util.Log;

import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;
import com.example.questease.Model.BDD.MotPendu;


import Service.ChoseAPI.ChoseAPI;
import Service.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandlerPenduAPI {
    private Context context;

    public HandlerPenduAPI(Context context) {
        this.context = context;
    }

    // Get random Chose with callback handling
    public void GetRandomChose(PenduCallBack callback) {

        MotPenduAPI motPenduAPI = RetrofitInstance.getRetrofitInstance().create(MotPenduAPI.class);
        Call<MotPendu> call = motPenduAPI.getRandomMotPendu();
        call.enqueue(new Callback<MotPendu>() {
            @Override
            public void onResponse(Call<MotPendu> call, Response<MotPendu> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.OnMotPenduReceived(response.body());
                } else {
                    Log.d("HandlerObjectAPI", "Request failed: " + response.code());
                    callback.OnFailure("Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MotPendu> call, Throwable t) {
                Log.d("HandlerObjectAPI", "Error getting motPendu object: " + t.getMessage());
            }


        });
    }
}