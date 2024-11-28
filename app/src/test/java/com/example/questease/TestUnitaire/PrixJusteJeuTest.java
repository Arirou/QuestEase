package com.example.questease.TestUnitaire;

import static org.junit.Assert.assertEquals;

import com.example.questease.Model.Jeu.PrixJusteJeu;

import org.junit.Test;

public class PrixJusteJeuTest {

    @Test
    public void getRemainingAttempts() {
        PrixJusteJeu game = new PrixJusteJeu(3, 42);
        assertEquals(3, game.getRemainingAttempts());
    }

    @Test
    public void checkGuess() {
        PrixJusteJeu game = new PrixJusteJeu(3, 42);
        assertEquals(PrixJusteJeu.Result.TROP_BAS, game.checkGuess(30));
        assertEquals(PrixJusteJeu.Result.TROP_HAUT, game.checkGuess(50));
    }

    static PrixJusteJeu prix;

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
