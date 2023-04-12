package ship.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    private List<Card> ownStack = new ArrayList<>(); // bez przypisania new ArrayList zbior jest zawsze null (brak listy).
    // gdy próbuję dodać co do listy null to mam NullPointerExc

    Game game = new Game();

    public Card draw() {
        List<Card> shuffled = game.shuffle();
        Card picked = shuffled.get(0);
        System.out.println(picked);
        shuffled.remove(picked);
        return picked;
    }

    public List<Card> getOwnStack() {
        return ownStack;
    }
}
