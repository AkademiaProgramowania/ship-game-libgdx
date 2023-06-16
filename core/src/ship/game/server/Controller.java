package ship.game.server;

import ship.game.server.events.Event;
import ship.game.server.events.EventBus;
import ship.game.server.events.EventListener;
import ship.game.server.events.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller implements EventListener {
    Scanner scanner = new Scanner(System.in);
    private Game game;  // czy potrzebna tu referencja doi gry skoro mamy eventy?

    public Controller(Game game) {
        this.game = game;
        EventBus.subscribe(EventType.TURN_START, this);
        EventBus.subscribe(EventType.GAME_END, this);
        EventBus.subscribe(EventType.DRAW_CARD, this);
        EventBus.subscribe(EventType.SELECT_CARDS_TO_RETURN, this);
        EventBus.subscribe(EventType.PLAYER_SWITCHED, this);
        EventBus.subscribe(EventType.SET_SHIP_TYPE_TO_COLLECT, this);

    }

    public void play() {
        System.out.println("Widzisz talie i graczy o numerach 1, 2");
        EventBus.notify(new Event(EventType.GAME_START));
    }

    public void playTurn(Event event) {
        System.out.println("Gra gracz " + event.getPlayer().playerIndex);
        EventBus.notify(new Event(EventType.DRAW_CARD_DECISION));
        do {
            decideOnNextTurn();
        } while (event.getPlayer().isStillPlaying());
    }

    public void decideOnNextTurn() {
        System.out.println("You need " + game.getCurrentPlayer().checkNumberOfMissingShipCards() + " ship cards");
        System.out.println("Collected ship type: " + game.getCurrentPlayer().getCollectedShipType());
        if (game.getCurrentPlayer().getShipsCollected(true).size() > 0) {
            System.out.println("Collected ships: " + game.getCurrentPlayer().getShipsCollected(true).toString());
        }
        System.out.println("1 - draw a card, 2 - buy ship, 3 - end turn, 4 - save players, 5 - get players from DB");
        switch (scanner.nextInt()) {
            case 1:
                EventBus.notify(new Event(EventType.DRAW_CARD_DECISION));
                break;
            case 2:
                if (game.getCurrentPlayer().getCards(Card.Type.COIN).size() < 3) {
                    System.out.println("Not enough coins - animacja");
                    break;
                }
                System.out.println("Give player number"); //todo żeby się nie zepsuło jak wybierze sam siebie
                int requestedPlayer = scanner.nextInt();

                Card purchased = game.getPlayerByIndex(requestedPlayer-1).getSelectedShipCard(game.getCurrentPlayer().getCollectedShipType());

                System.out.println("Selected: " + purchased);
                if (purchased == null) {
                    break;
                }
                Event event = new Event(EventType.CARD_PURCHASE_DECISION);
                event.setPlayer(game.getPlayerByIndex(requestedPlayer));
                event.setCard(purchased);
                EventBus.notify(event);
                break;
            case 3:
                EventBus.notify(new Event(EventType.PASS_DECISION));
                break;
            case 4:
                System.out.println("4");
                game.savePlayers();
                break;
            case 5:
                System.out.println("5");
                System.out.println(game.getPlayersFromDB());
        }
    }

    public void doStorm(Player player) {
        System.out.println("Coins " + player.getCards(Card.Type.COIN).size());
        System.out.println("Cannons " + player.getCards(Card.Type.CANNON).size());
        System.out.println("Ships collected " + player.getShipsCollected(true).size());
        System.out.println("Ships not collected " + player.getShipsCollected(false).size());
        int sum = 0;
        if (player.chceckIfMoreThan3()) {
            do {
                System.out.println("Select a card and return it. Missing points:  " + (3 - sum));
                System.out.println("1 - coin, 2 - cannon, 3 - ship, 4 - ship collected");
                //wyswietlanie ile masz czego
                switch (scanner.nextInt()) {
                    case 1:
                        if (!player.getCards(Card.Type.COIN).isEmpty()) {
                            Card card = player.getCards(Card.Type.COIN).get(0);
                            sum++;// sumowanie dla pętli
                            game.addToTemporaryStack(card); // setowanie ownera karty w metodzie
                            player.removeCard(card);
                        }
                        break;
                    case 2:
                        if (!player.getCards(Card.Type.CANNON).isEmpty()) {
                            Card card = player.getCards(Card.Type.CANNON).get(0);
                            sum += 3;
                            game.addToTemporaryStack(card);
                            player.removeCard(card);
                        }
                        break;
                    case 3:
                        if (!player.getShipsCollected(false).isEmpty()) {
                            Card card = player.getShipsCollected(false).get(0);
                            sum++;
                            game.addToTemporaryStack(card);
                            player.removeCard(card);
                        }
                        break;
                    case 4:
                        if (!player.getShipsCollected(true).isEmpty()) {
                            Card card = player.getShipsCollected(true).get(0);
                            sum++;
                            game.addToTemporaryStack(card);
                            player.removeCard(card);
                        }
                        if (player.getShipsCollected(true).size() == 0) {
                            player.setCollectedShipType(null);
                        }
                        break;
                    default:
                        System.out.println("Choose cards");
                }
            } while (sum < 3 && player.hasCards()); //ma mniej niż 3 oraz ma karty
        } else {
            List<Card> all = new ArrayList<>(); // robocza lista do skorzystania z metody addToTemporaryStack
            all.addAll(player.getOwnStack());
            for (Card card : all) {
                game.addToTemporaryStack(card); // setowanie ownera w metodzie
            }
            player.getOwnStack().clear();
            player.setCollectedShipType(null);
            }
        }

    public void endGame(Event event) {
        System.out.println("Game ends. \nPlayer " + event.getPlayer().playerIndex + " wins, having collected " + event.getPlayer().getCollectedShipType() + " ship type");
        System.exit(0);
    }

    @Override
    public void react(Event event) {
        EventType eventType = event.getType();
        if (eventType == EventType.TURN_START) {
            playTurn(event);
        }
        if (eventType == EventType.GAME_END) {
            endGame(event);
        }
        if (eventType == EventType.DRAW_CARD) {
            Card card = event.getCard();

            if (card.getType().equals(Card.Type.COIN)) {
                System.out.println("Animacja przejscia na stos monet");
            } else if (card.getType().equals(Card.Type.CANNON)) {
                System.out.println("Animacja przejscia na stos dzial");
            } else if (card.getType().equals(Card.Type.STORM)) {
                System.out.println("Reakcja na wyciagniecie karty - animacja burzy");
                doStorm(event.getPlayer());
                event.getPlayer().showOwnStack();
                System.out.println("Collected ship type: " + event.getPlayer().getCollectedShipType());
                game.switchToNextPlayer();
            } else if (card.getType().equals(Card.Type.SHIP) && event.getPlayer().isCollectingThisShip(card)) {
                System.out.println("Animacja przejscia na stos statkow do kolekcjonowania");
            } else {
                System.out.println("Animacja przejscia na stos statkow do handlu");
            }
        }
        if (eventType == EventType.PLAYER_SWITCHED) {
            System.out.println("Player switched. Current: " + event.getPlayer().playerIndex);
            playTurn(event);
        }
    }
}