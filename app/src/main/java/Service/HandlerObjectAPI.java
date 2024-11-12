package Service;

import android.content.Context;
import android.util.Log;

import Model.BDD.ChoseATrouverPrixJuste;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandlerObjectAPI {
    private Context context;

    public HandlerObjectAPI(Context context) {
        this.context = context;
    }

    // Get random Chose with callback handling
    public void GetRandomChose(ChoseCallback callback) {
        ChoseAPI lobbyApi = RetrofitInstance.getRetrofitInstance().create(ChoseAPI.class);
        Call<Model.BDD.ChoseATrouverPrixJuste> call = lobbyApi.getChoseRandom();
        call.enqueue(new Callback<ChoseATrouverPrixJuste>() {
            @Override
            public void onResponse(Call<ChoseATrouverPrixJuste> call, Response<ChoseATrouverPrixJuste> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onChoseReceived(response.body());
                } else {
                    Log.d("HandlerObjectAPI", "Request failed: " + response.code());
                    callback.onFailure("Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChoseATrouverPrixJuste> call, Throwable t) {
                Log.d("HandlerObjectAPI", "Error getting Chose object: " + t.getMessage());
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }
}