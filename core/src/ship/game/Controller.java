package ship.game;

import java.util.Scanner;

public class Controller {
    Scanner scanner = new Scanner(System.in);

    private Game game;

    public Controller(Game game) {
        this.game = game;
    }

    public void play() {
        game.shuffle(game.getMainStack());
        playTurn();
    }

    public void playTurn() {
        /*System.out.println("Player " + game.getCurrentPlayer().getPlayerNum());
        int missing = game.checkNumberOfMissingShipCards();
        System.out.println("You need " + missing + " ship pieces");*/
        game.passCardToAPlayerIfNotStorm(); // tu jest draw()
        decideOnNextTurn();

    }

    public void decideOnNextTurn() {
        System.out.println("1 - draw a card, 2 - buy ship card, 3 - end your turn");
        if (scanner.nextInt() == 1) {
            playTurn();
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
}