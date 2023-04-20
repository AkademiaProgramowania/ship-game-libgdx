package ship.game;

import ship.game.events.*;

import java.util.Scanner;

public class Controller implements EventListener {
    Scanner scanner = new Scanner(System.in);

    private Game game;

    public Controller(Game game) {
        this.game = game;
        EventBus.subscribe(EventType.CARD_DRAWN, this);
    }

    public void play() {
        playTurn();
        playTurn();
        playTurn();
        playTurn();
    }

    public void playTurn() {
        game.passCardToAPlayerIfNotStorm();
        decideOnNextTurn();

    }

    public void decideOnNextTurn() {
        int missing = game.giveNumberOfMissingShipCards();
        System.out.println("You miss" + missing + " ship pieces");
        System.out.println("1 - end your turn, 2 - buy ship card");
        if (scanner.nextInt() == 1) {
            game.endTurn();
        }
        if (scanner.nextInt() == 2) {
            System.out.println("Choose a player");
            int player = scanner.nextInt();
            String requested = game.getCurrentPlayer().collectedShipType;
            game.buyShipCard(player, requested);
        }
    }

    @Override
    public void react(Event event) {
        if (event.getType() == EventType.CARD_DRAWN) {
            System.out.println("Drawn " + event.getCard());
        }
    }
}