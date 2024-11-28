Feature: En tant qu'utilisateur
  je souhaite supposer le prix de l'objet
  Afin de gagner au prix Juste

  Background: L'utilisateur est en jeu avec un second individu
    Given L'utilisateur est dans le jeu du prix Juste
    And C'est le tour de l'utilisateur

  Scenario: L'utilisateur trouve le bon prix
    Given L'utilisateur joue au prix Juste
    When L'utilisateur donne le bon prix
    Then L'utilisateur gagne le jeu

  Scenario: L'utilisateur ne donne pas le bon prix
    Given L'utilisateur joue au prix Juste
    When L'utilisateur donne un mauvais prix
    Then Le nombre de tentative est réduit de un
    And C'est le tour du second individu
