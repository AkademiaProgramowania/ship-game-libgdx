package ship.game;

public class Launcher {

    public void play() {
        Game game = new Game();
        Player player1 = new Player(game);
        Player player2 = new Player(game);
        Controller controller = new Controller(game, player1);

        game.addPlayer(player1);
        game.addPlayer(player2);






    }
}
