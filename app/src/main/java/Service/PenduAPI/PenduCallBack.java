package Service.PenduAPI;

import com.example.questease.Model.BDD.MotPendu;

public interface PenduCallBack {
    void OnMotPenduReceived(MotPendu motPendu);
    void OnFailure(String errorMessage);
}
