package ship.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    Game game = new Game();

    public Card draw() {
        List<Card> shuffled = game.shuffle();
        Card picked = shuffled.get(0);
        System.out.println(picked);
        shuffled.remove(picked);
        return picked;
    }
}
