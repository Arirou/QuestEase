package com.example.questease.TestIntegration;

import static com.example.questease.Model.Jeu.PrixJusteJeu.Result.CORRECT;

import android.util.Log;

import com.example.questease.Controller.ChoseATrouverPrixJusteHandler;
import com.example.questease.Model.Jeu.PrixJusteJeu;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PrixJusteSteps {

    private final ChoseATrouverPrixJusteHandler PrixJusteHandler;

    private static PrixJusteJeu PJ;


    public PrixJusteSteps(ChoseATrouverPrixJusteHandler prixJusteHandler) {
        PrixJusteHandler = prixJusteHandler;
    }


    @Given("L'utilisateur est dans le jeu du prix Juste")
    public void lancement_de_jeu(){
        PJ = new PrixJusteJeu(2, 5);
    }

    @And("C'est le tour de l'utilisateur")
    public void verif(){

    }

    //L'utilisateur trouve le bon prix
    @Given("L'utilisateur joue au prix Juste")
    public void est_en_jeu_correct(){

    }

    @When("L'utilisateur donne le bon prix")
    public void bon_prix(){
        PJ.checkGuess(5);
    }

    @Then("L'utilisateur gagne le jeu")
    public void victoire(){
        if(PJ.checkGuess(5) == CORRECT){
            Log.e("Victoire", "Felicitation, vous avez gagne");
        }
    }


    //L'utilisateur ne donne pas le bon prix
    @Given("L'utilisateur joue au prix Juste")
    public void est_en_jeu_erreur(){

    }

    @When("L'utilisateur donne un mauvais prix")
    public void mauvais_prix(){

    }

    @Then("Le nombre de tentative est réduit de un")
    public void pas_de_victoire(){

    }

    @And("C'est le tour du second individu")
    public void et_on_repart(){
        
    }
}
