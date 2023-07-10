package ship.game.server;

import java.util.List;

public interface Repository {

    int savePlayers(List<Player> players);
    int saveCards(List<Card> cards, int index);


}
