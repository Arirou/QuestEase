package Service.ChoseAPI;


//TODO Reprendre les paramètrs du manifest

import com.example.questease.Model.BDD.ChoseATrouverPrixJuste;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChoseAPI {
    @GET("/choseATrouver")
    Call<List<ChoseATrouverPrixJuste>> getMotChose();

    @GET("/choseATrouver/random")
    Call<ChoseATrouverPrixJuste> getChoseRandom();
}