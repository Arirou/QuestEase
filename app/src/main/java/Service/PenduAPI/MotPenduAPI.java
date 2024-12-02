package Service.PenduAPI;

import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;
import com.example.questease.Model.BDD.MotPendu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MotPenduAPI {
    @GET("/chose")
    Call<List<ChoseATrouverPrixJuste>> getMotChose();

    @GET("/motpendu/random")
    Call<MotPendu> getRandomMotPendu();
}
