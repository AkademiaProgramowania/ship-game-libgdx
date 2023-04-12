package ship.game;

import com.sun.tools.jdeprscan.scan.Scan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Controller {
    Scanner scanner = new Scanner(System.in);
    Player player1 = new Player();

    boolean available = true;

    public void chooseStack() { // zmienić nazwę
        Card picked = player1.draw(); // czy picked przekazywać w argumencie?
        System.out.println(picked);
        List<Card> ownStack = player1.getOwnStack(); // List.copyOf?
        //List<Card> all = player1.game.getAllCards();

        if (picked.getType().equals("coin")) {
            ownStack.add(picked);
            System.out.println("Super! Another coin");
        }
        if (picked.getType().equals("cannon")) {
            ownStack.add(picked);
            System.out.println("Super! A cannon is always handy");
        }
        if (picked.getType().contains("S") && available) {
            available = false;
            ownStack.add(picked);
            System.out.println("Another ship piece to your collection");
        }
        if (picked.getType().contains("S") && !available) { // czy to trzeba całe powtarzać?
            ownStack.add(picked);
            System.out.println("A ship card not of your choice goes to a stack");
        }
        if (picked.getType().equals("storm")) {
            System.out.println("A storm has come! Return three cards back to the pile");
            chooseWhichToReturn();
        }
        System.out.println("Pokaz zbior");
        showOwnStack();
    }

    public void chooseWhichToReturn() {
        int num = 0;
        showOwnStack();
        List<Card> toReturn = new ArrayList<>();

        while (num <= 3) {
            System.out.println("Choose a card to return");
            System.out.println("You need " + (3 - num) + " cards more");
            System.out.println("For a ship enter 1");
            System.out.println("For a coin enter 2");
            System.out.println("For a cannon enter 3");
            int entered = scanner.nextInt();

            Card cardShip = player1.game.findCardByType("ship");
            Card cardCoin = player1.game.findCardByType("coin");
            Card cardCannon = player1.game.findCardByType("cannon");
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
        player1.game.getMainStack().addAll(toReturn);
        //spr czy w mainStack jest zawartość toReturn
    }

    public void showOwnStack() {
        List<Card> ownStack = player1.getOwnStack(); // List.copyOf?
        List<Card> ships = new ArrayList<>();
        List<Card> coins = new ArrayList<>();
        List<Card> cannons = new ArrayList<>();
        for (Card card : ownStack) {
            if (card.equals(player1.game.findCardByType("ship"))) {
                ships.add(card);
            }
            if (card.equals(player1.game.findCardByType("coin"))) {
                coins.add(card);
            }
            if (card.equals(player1.game.findCardByType("cannon"))) {
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