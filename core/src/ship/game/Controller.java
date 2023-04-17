package ship.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    Scanner scanner = new Scanner(System.in);
    Player player;
    private final Card drawn = null;
    boolean available = true;
    private final List<Card> ownStack = new ArrayList<>();
    private Game game;

    public Controller(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public void putDrawnCardInOwnStack() { // zmienić nazwę
         // czy drawn przekazywać w argumencie? Musi być dostępne dla innych metod
        // i nie powtarzać metody draw
        System.out.println("Drawn " + drawn);

        // karta jest wyciągana ze stosu
        // i jest przekazywana do odp podzbioru w ownStack
        // wszystkie karty ship najpierw idą do ownStack i są spr czy available w metodzie toReturn
        // jak spr czy to piersza karta statku? Przy karcie statku iterowac po zbiorze i spr czy jest SHIP + shipNum

        if (drawn.getType().equals(Card.Type.SHIP) || drawn.getType().equals(Card.Type.COIN) || drawn.getType().equals(Card.Type.CANNON)) {
            ownStack.add(drawn);
            System.out.println("A card " + drawn + " goes to your stack");
        }
        if (drawn.getType().equals(Card.Type.STORM)) {
            System.out.println("A storm has come! You must return three cards back to the pile");
            chooseWhichToReturn();
        }

        System.out.println("Pokaz zbior");
        showOwnStack();
    }

    public void checkIfShipCardIsToCollect(String shipNum) { // zdecydować kiedy dzielimy karty w stacku na osobne zbiory po SHIP oraz shipNum
        // przy pierwszym wyciągnięciu SHIP jego shipNum staje się "collected",
        // pozostałe SHIP shipNum są available
        if (drawn.getType().equals(Card.Type.SHIP)) { // jeśli wyciągnie ship, to
            for (Card card : ownStack) { // to leci po zbiorze ownStack i spr czy jest SHIP + ten sam shipNum
                if (card.getType().equals(Card.Type.SHIP)) { // i spr czy dopisać spr po shipNum
                }
            }
        }
    }

    public void chooseWhichToReturn() {
        showOwnStack();
        List<Card> toReturn = new ArrayList<>();
        int num = 0;

        while (num <= 3) {
            System.out.println("Choose a card to return");
            System.out.println("You need " + (3 - num) + " cards more");
            System.out.println("For a ship enter 1");
            System.out.println("For a coin enter 2");
            System.out.println("For a cannon enter 3");
            int entered = scanner.nextInt();

            Card cardShip = player.findCardByTypeInOwnStack(Card.Type.SHIP); // filtrować available czy nie potrzeba - spr
            Card cardCoin = player.findCardByTypeInOwnStack(Card.Type.COIN);
            Card cardCannon = player.findCardByTypeInOwnStack(Card.Type.CANNON);
            switch (entered) {
                case 1:
                    toReturn.add(cardShip);
                    num++;
                case 2:
                    toReturn.add(cardCoin);
                    num++;
                case 3:
                    toReturn.add(cardCannon);
                    num++;
            }
            System.out.println("3 cards are back to the main stack: ");
            for (Card card : toReturn) {
                System.out.println(card);
            }
        }
        game.getMainStack().addAll(toReturn);
        //spr czy w mainStack jest zawartość toReturn
    }

    public void showOwnStack() {
        List<Card> ships = new ArrayList<>();
        List<Card> coins = new ArrayList<>();
        List<Card> cannons = new ArrayList<>();
        for (Card card : ownStack) {
            if (card.equals(player.findCardByTypeInOwnStack(Card.Type.SHIP))) {
                ships.add(card);
            }
            if (card.equals(player.findCardByTypeInOwnStack(Card.Type.COIN))) {
                coins.add(card);
            }
            if (card.equals(player.findCardByTypeInOwnStack(Card.Type.CANNON))) {
                cannons.add(card);
            }
        }
        for (Card card1 : ships) {
            System.out.println("ship " + card1);
        }
        for (Card card1 : coins) {
            System.out.println("coin " + card1);
        }
        for (Card card1 : cannons) {
            System.out.println("cannon " + card1);
        }
    }
}


    /*case 1:
            player1.game.getMainStack().add(cardShip);
            num++;
            case 2:
            player1.game.getMainStack().add(cardCoin);
            num++;
            case 3:
            player1.game.getMainStack().add(cardCannon);
            num++;*/