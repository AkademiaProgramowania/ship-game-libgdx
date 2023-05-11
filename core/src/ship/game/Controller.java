package ship.game;

import ship.game.events.Event;
import ship.game.events.EventBus;
import ship.game.events.EventListener;
import ship.game.events.EventType;

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
        System.out.println("Gra gracz " + event.getPlayer().playerNum);
        EventBus.notify(new Event(EventType.DRAW_CARD_DECISION));
        do {
            decideOnNextTurn();
        } while (event.getPlayer().stillPlaying);
    }

    public void selectCardsToReturn() {

    }

    public void decideOnNextTurn() {
        System.out.println("You need " + game.getCurrentPlayer().checkNumberOfMissingShipCards() + " ship cards");
        System.out.println("1 - draw a card, 2 - buy ship card, 3 - end your turn");
        switch (scanner.nextInt()) {
            case 1:
                EventBus.notify(new Event(EventType.DRAW_CARD_DECISION));
                break;
            case 2:
                // jak przekazać dane z konsoli do game? player (player index), card (type, num)
                EventBus.notify(new Event(EventType.CARD_PURCHASE_DECISION));
                break;
            case 3:
                EventBus.notify(new Event(EventType.PASS_DECISION));
                break;
        }
    }

    //zrobić najłatwiej jak się da
    public void doStorm(Player player) {
        int sum = 0;
        do {
            // tu tylko kliki, wartość zwróconych kart zlicza game
            System.out.println("Select a card to return it");
            System.out.println("1 - coin, 2 - cannon, 3 - ship, 4 - ship collected");
            //wyswietlanie ile masz czego
            switch (scanner.nextInt()) {
                case 1:
                    if (!player.getCards(Card.Type.COIN).isEmpty()) {
                        Card card = player.getCards(Card.Type.COIN).get(0);
                        player.removeCard(card);
                        sum++;
                    }
                    break;
                case 2:
                    if (!player.getCards(Card.Type.CANNON).isEmpty()) {
                        Card card = player.getCards(Card.Type.CANNON).get(0);
                        player.removeCard(card);
                        sum += 3;
                    }
                    break;
                case 3:
                    if (!player.getShipsCollected(true).isEmpty()) {
                        Card card = player.getShipsCollected(true).get(0);
                        player.removeCard(card);
                        sum++;
                    }
                    break;
                case 4:
                    if (!player.getShipsCollected(false).isEmpty()) {
                        Card card = player.getShipsCollected(false).get(0);
                        player.removeCard(card);
                        sum++;
                    }
                    break;
                default:
                    System.out.println("Click to return cards");
            }
        } while (sum < 3 && player.hasCards()); //ma mniej niż 3 oraz ma karty
    }

    public void endGame(Event event) {
        System.out.println("Game ends. Player " + event.getPlayer().playerNum + " wins");
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
            } else if (card.getType().equals(Card.Type.SHIP) && event.getPlayer().isCollectingThisShip(card)) {
                System.out.println("Animacja przejscia na stos statku do kolekcjonowania");
            } else {
                System.out.println("Animacja przejscia na stos statkow do handlu");
            }
        }
        if (eventType == EventType.SELECT_CARDS_TO_RETURN) {
            selectCardsToReturn();
        }
        if (eventType == EventType.PLAYER_SWITCHED) {
            System.out.println("Zmiana gracza na " + event.getPlayer().playerNum);
            playTurn(event);
        }
        // test : zmiana event.getPlayer().get... na game.getCurrentPlayer.get...
    }
}