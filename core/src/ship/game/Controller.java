package ship.game;

import ship.game.events.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller implements EventListener {
    Scanner scanner = new Scanner(System.in);

    private Game game;

    public Controller(Game game) {
        this.game = game;
        EventBus.subscribe(EventType.CARD_DRAWN,this);
    }

    public void play() {
        playTurn();
        playTurn();
        playTurn();
        playTurn();
    }

    public void playTurn() {
        game.passToAPlayerIfNotStorm();
        decideOnContinuation();

    }

    public void decideOnContinuation() {
        System.out.println("Want to pass? 1 yes, 2 no");
        if (scanner.nextInt() == 1) {
            game.pass();
        }
    }


   /* public void checkIfShipCardIsToCollect(String shipNum) { // zdecydować kiedy dzielimy karty w stacku na osobne zbiory po SHIP oraz shipNum
        // przy pierwszym wyciągnięciu SHIP jego shipNum staje się "collected",
        // pozostałe SHIP shipNum są available
        if (drawn.getType().equals(Card.Type.SHIP)) { // jeśli wyciągnie ship, to
            for (Card card : ownStack) { // to leci po zbiorze ownStack i spr czy jest SHIP + ten sam shipNum
                if (card.getType().equals(Card.Type.SHIP)) { // i spr czy dopisać spr po shipNum
                }
            }
        }
    }*/

    public void chooseWhichToReturn() { // gdzie umieścić metodę żeby mieć dostęp?
        //showOwnStack();
        List<Card> toReturn = new ArrayList<>();
        int num = 0;

        while (num <= 3) {
            System.out.println("Choose a card to return");
            System.out.println("You need " + (3 - num) + " cards more");
            System.out.println("For a ship enter 1");
            System.out.println("For a coin enter 2");
            System.out.println("For a cannon enter 3");
            int entered = scanner.nextInt();

            Card cardShip = game.getCurrentPlayer().findShipCardToReturn();
            Card cardCoin = game.getCurrentPlayer().findCardByTypeInOwnStack(Card.Type.COIN);
            Card cardCannon = game.getCurrentPlayer().findCardByTypeInOwnStack(Card.Type.CANNON);
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

    @Override
    public void react(Event event) {
        if (event.getType()==EventType.CARD_DRAWN) {
            System.out.println("Drawn " + event.getCard());
        }
    }

/*    public void showOwnStack() {
        List<Card> ships = new ArrayList<>();
        List<Card> coins = new ArrayList<>();
        List<Card> cannons = new ArrayList<>();
        for (Card card : ownStack) {
            if (card.equals(game.getCurrentPlayer().findCardByTypeInOwnStack(Card.Type.SHIP))) {
                ships.add(card);
            }
            if (card.equals(game.getCurrentPlayer().findCardByTypeInOwnStack(Card.Type.COIN))) {
                coins.add(card);
            }
            if (card.equals(game.getCurrentPlayer().findCardByTypeInOwnStack(Card.Type.CANNON))) {
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
    }*/
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