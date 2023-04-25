package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

import java.util.Scanner;

public class Controller implements EventListener {
    Scanner scanner = new Scanner(System.in);
    Player currentPlayer;

    private Game game;

    public Controller(Game game) {
        this.game = game;
        EventBus.subscribe(EventType.TURN_START, this);
        EventBus.subscribe(EventType.GAME_END, this);
        EventBus.subscribe(EventType.DRAW_CARD, this);


    }

    public void play() {
        EventBus.notify(new Event(EventType.GAME_START));
        System.out.println("Widzisz talie i graczy o numerach 1, 2");
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


    public void decideOnNextTurn() {
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
    }

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
                System.out.println("Animacja przejścia na stos monet");
            }
            if (event.getCard().getType().equals(Card.Type.CANNON)) {
                System.out.println("Animacja przejścia na stos dział");
            }
            if (event.getCard().getType().equals(Card.Type.SHIP) && (!event.getCard().getSecondShipType().equals(currentPlayer.collectedShipType))) {
                System.out.println("Animacja przejścia na stos statków do handlu");
            }
            if (event.getCard().getType().equals(Card.Type.SHIP) && (event.getCard().getSecondShipType().equals(currentPlayer.collectedShipType))) {
                System.out.println("Animacja przejscia na stos statków do kolekcjonowania");
            }
            if (event.getCard().getType().equals(Card.Type.STORM)) {
                System.out.println("Animacja burzy");
            }
        }
    }
}