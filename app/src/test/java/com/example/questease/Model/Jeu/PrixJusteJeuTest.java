package com.example.questease.Model.Jeu;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PrixJusteJeuTest {

    static PrixJusteJeu prix;
    @Test
    public void getRemainingAttempts() {
        prix = new PrixJusteJeu(3, 42);
        assertEquals(3, prix.getRemainingAttempts());
    }

    @Test
    public void checkGuess() {
        prix = new PrixJusteJeu(3, 42);
        assertEquals(PrixJusteJeu.Result.TROP_BAS, prix.checkGuess(30));
        assertEquals(PrixJusteJeu.Result.TROP_HAUT, prix.checkGuess(50));
    }



    @Test
    public void testSuperieur() {
        prix = new PrixJusteJeu(5,50);
        System.out.println("Running test 1");
        assertEquals(PrixJusteJeu.Result.TROP_HAUT, prix.checkGuess(75));
        assertEquals(4, prix.getRemainingAttempts());
    }

    @Test
    public void testInferieur(){
        prix = new PrixJusteJeu(5,50);
        System.out.println("Running test 2");
        assertEquals(PrixJusteJeu.Result.TROP_BAS, prix.checkGuess(40));

    }
    @Test
    public void testEgal(){
        prix = new PrixJusteJeu(5,50);
        System.out.println("Running test 3");
        assertEquals(PrixJusteJeu.Result.CORRECT, prix.checkGuess(50));
    }

}