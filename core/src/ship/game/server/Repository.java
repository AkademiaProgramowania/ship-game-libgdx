package ship.game.server;

import java.util.List;

public interface Repository { //InMemoryRepository

    int savePlayers(List<Player> players);
    int createTableCards(); //todo, repository może samo sobie przygotować wszystko co potrzebuje do zapisywnia
    int saveCards(List<Card> cards, int index);

    List<Player> getPlayersFromDB();

    List<Card> getCardsFromDB(int ownerIndex);
}
