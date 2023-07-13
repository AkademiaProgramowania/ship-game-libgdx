package ship.game.server;

import java.util.List;

public interface Repository {

    int savePlayers(List<Player> players);
    int createTableCards();
    int saveCards(List<Card> cards, int index);

    List<Player> getPlayersFromDB();

    List<Card> getCardsFromDB(int ownerIndex);
}
