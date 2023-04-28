package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

import java.util.Scanner;

public class Controller implements EventListener {
    Scanner scanner = new Scanner(System.in);
    private Game game;

    public Controller(Game game) {
        this.game = game;
        EventBus.subscribe(EventType.TURN_START, this);
        EventBus.subscribe(EventType.GAME_END, this);
        EventBus.subscribe(EventType.DRAW_CARD, this);
        EventBus.subscribe(EventType.DO_STORM, this);
        EventBus.subscribe(EventType.PLAYER_SWITCHED, this);
        EventBus.subscribe(EventType.SHIP_TYPE_TO_COLLECT, this);


    }

    public void play() {
        System.out.println("Widzisz talie i graczy o numerach 1, 2");
        EventBus.notify(new Event(EventType.GAME_START));
    }

    public void playTurn(Event event) {
        System.out.println("Rozpoczyna gre gracz " + event.getPlayer().playerNum);
        EventBus.notify(new Event(EventType.DRAW_CARD_DECISION));

        /*System.out.println("Player " + game.getCurrentPlayer().getPlayerNum());
        int missing = game.checkNumberOfMissingShipCards();
        System.out.println("You need " + missing + " ship pieces");*/
        /*game.passCardToAPlayerIfNotStorm(); // tu jest draw()
        decideOnNextTurn();
*/
    }

    public void doStorm() {
        // tylko kliki
        // wartość zwrócinych kart zlicza game

        System.out.println("Click on a card to return it");
        System.out.println("1 - coin, 2 - cannon, 3 - ship, 4 - ship collected");
        switch (scanner.nextInt()) {
            case 1:
                EventBus.notify(new Event(EventType.CLICK_ON_COIN));
                break;
            case 2:
                EventBus.notify(new Event(EventType.CLICK_ON_CANNON));
                break;
            case 3:
                EventBus.notify(new Event(EventType.CLICK_ON_SHIP));
                break;
            case 4:
                EventBus.notify(new Event(EventType.CLICK_ON_SHIP_COLLECTED));
                break;
            default:
                System.out.println("Click to return cards");
        }
    }

/*    public void decideOnNextTurn() {
        System.out.println("1 - draw a card, 2 - buy ship card, 3 - end your turn");
        if (scanner.nextInt() == 1) {
            //playTurn();
            // tu event z decyzjami
        }
        if (scanner.nextInt() == 2) {
            System.out.println("Choose a player");
            int player = scanner.nextInt();
            String requested = game.getCurrentPlayer().collectedShipType;
            game.buyShipCard(player, requested);
        }
        if (scanner.nextInt() == 3) {
            System.out.println("Your turn ends");
            game.endTurn();
        }
    }*/

    public void endGame(Event event) {
        System.out.println("Game ends. Player " + event.getPlayer().playerNum + " wins");
    }

    @Override
    public void react(Event event) {
        if (event.getType() == EventType.TURN_START) {
            playTurn(event);
        }
        if (event.getType() == EventType.GAME_END) {
            endGame(event);
        }
        if (event.getType() == EventType.DRAW_CARD) {
            if (event.getCard().getType().equals(Card.Type.COIN)) {
                System.out.println("Animacja przejscia na stos monet");
            }
            if (event.getCard().getType().equals(Card.Type.CANNON)) {
                System.out.println("Animacja przejscia na stos dzial");
            }
            if (event.getCard().getType().equals(Card.Type.SHIP) && (!event.getCard().getSecondShipType().equals(event.getPlayer().collectedShipType))) {
                System.out.println("Animacja przejscia na stos statkow do handlu");
            }
            if (event.getCard().getType().equals(Card.Type.SHIP) && (event.getCard().getSecondShipType().equals(event.getPlayer().collectedShipType))) {
                System.out.println("Animacja przejscia na stos statkow do kolekcjonowania");
            }
            if (event.getCard().getType().equals(Card.Type.STORM)) {
                System.out.println("Animacja burzy");
                doStorm();
            }
        }
        if (event.getType() == EventType.DO_STORM) {
            doStorm();
        }
        if (event.getType() == EventType.PLAYER_SWITCHED) {
            System.out.println("Zmiana gracza na " + event.getPlayer().playerNum);
        }
        if (event.getType() == EventType.SHIP_TYPE_TO_COLLECT) {
            System.out.println(event.getPlayer().getCollectedShipType() + " type set for player " + event.getPlayer().getPlayerNum());
        }
    }
}