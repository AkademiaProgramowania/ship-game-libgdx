package ship.game.server;

import ship.game.server.events.Event;
import ship.game.server.events.EventBus;
import ship.game.server.events.EventListener;
import ship.game.server.events.EventType;

import java.util.ArrayList;
import java.util.Collections;
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
        EventBus.subscribe(EventType.PLAYER_SWITCHED, this);
    }

    public void play() {
        System.out.println("1 - new game, 2 - restore game");
        String input = scanner.next();
        if (input.equals("1")) {
            EventBus.notify(new Event(EventType.GAME_START));
        } else {
            restoreGame();
        }
    }

    public void playTurn(Event event) {
        System.out.println("\nGra gracz " + event.getPlayer().getPlayerIndex());
        EventBus.notify(new Event(EventType.DRAW_CARD_DECISION));
        do {
            decideOnNextTurn();
        } while (event.getPlayer().getPlayingStatus().equals("T")); // todo spr czy działa po zmianie warunku
    }

    public void decideOnNextTurn() {
        System.out.println("Gra gracz " + game.getCurrentPlayer().getPlayerIndex());
        System.out.println("You need " + game.getCurrentPlayer().checkNumberOfMissingShipCards() + " ship cards");
        System.out.println("Collected ship type: " + game.getCurrentPlayer().getCollectedShipType());

        System.out.println("1 - draw a card, 2 - buy ship, 3 - end turn, 4 - save game");
        switch (scanner.nextInt()) {
            case 1:
                EventBus.notify(new Event(EventType.DRAW_CARD_DECISION));
                break;
            case 2:
                if (game.getCurrentPlayer().getCards(Card.Type.COIN).size() < 3) {
                    System.out.println("Not enough coins - animacja");
                    break;
                }
                System.out.println("Give player index"); //todo żeby się nie zepsuło jak wybierze sam siebie
                int requestedPlayer = scanner.nextInt();
                Card purchased = game.getPlayerByIndex(requestedPlayer - 1).getSelectedShipCard(game.getCurrentPlayer().getCollectedShipType());
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
                game.saveGame();
                restoreGame();
                break;
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

                switch (scanner.nextInt()) {
                    case 1:
                        if (!player.getCards(Card.Type.COIN).isEmpty()) {
                            Card card = player.getCards(Card.Type.COIN).get(0);
                            sum++;
                            game.addToTemporaryStack(card);
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
                            // gdy player nazbierał więcej innych statków niż własny i chce podmienić typ kolekcjonowany
                            setCollectedFromRemainingShips(player);
                        }
                        break;
                    default:
                        System.out.println("Choose cards");
                }
            } while (sum < 3 && player.hasCards()); //ma mniej niż 3 oraz ma karty
        } else {
            player.setCollectedShipType(null);
            List<Card> all = new ArrayList<>(player.getOwnStack());
            for (Card card : all) {
                game.addToTemporaryStack(card);
            }
            player.getOwnStack().clear();
        }
    }

    public void setCollectedFromRemainingShips(Player player) {
        List<Card> ships = player.getCards(Card.Type.SHIP);
        List<Card> shipsAvailable = new ArrayList<>();
        int numS1 = 0;
        int numS2 = 0;
        int numS3 = 0;
        int numS4 = 0;
        for (Card ship : ships) {
            if (game.shipIsNotCollected(ship)) {
                shipsAvailable.add(ship);
            }
            if (!shipsAvailable.isEmpty()) {
                for (Card card : shipsAvailable) { // spr którego typu najwięcej
                    switch (card.getSecondShipType()) {
                        case "S1":
                            numS1++;
                            break;
                        case "S2":
                            numS2++;
                            break;
                        case "S3":
                            numS3++;
                            break;
                        default:
                            numS4++;
                            break;
                    }
                    List<Integer> numOfShips = new ArrayList<>();
                    numOfShips.add(numS1);
                    numOfShips.add(numS2);
                    numOfShips.add(numS3);
                    numOfShips.add(numS4);
                    Collections.sort(numOfShips);
                    int intMax = Collections.max(numOfShips);
                    if (intMax == numS1) {
                        player.setCollectedShipType("S1");
                    } else if (intMax == numS2) {
                        player.setCollectedShipType("S2");
                    } else if (intMax == numS3) {
                        player.setCollectedShipType("S3");
                    } else {
                        player.setCollectedShipType("S4");
                    }
                }
            } else {
                System.out.println("Nie ma dostępnych statków");
            }
        }
        System.out.println("Collected po zmianie: " + player.getCollectedShipType() + " ilosc: " + player.getShipsCollected(true).size());
    }

    public void endGame(Event event) {
        System.out.println("Game ends. \nPlayer " + event.getPlayer() + " wins, having collected " + event.getPlayer().getCollectedShipType() + " ship type");
        System.exit(0);
    }

    public void restoreGame() {
        System.out.println("To restart press 1");
        if (scanner.nextInt() == 1) {
            game.restorePlayersFromDB();
            game.restoreCardsFromDB();
            System.out.println("Player (1): " + game.getPlayers().get(0).getOwnStack().size() + " " + game.getPlayers().get(0).getOwnStack());
            System.out.println("Player (2): " + game.getPlayers().get(1).getOwnStack().size() + " " + game.getPlayers().get(1).getOwnStack());
            System.out.println("MainStack: " + game.getMainStack().size() + " " + game.getMainStack());
            System.out.println("TemporaryStack: " + game.getTemporaryStack().size() + " " + game.getTemporaryStack());
            EventBus.notify(new Event(EventType.GAME_START));
        } else {
            System.exit(0);
        }
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
            playTurn(event);
        }
    }
}