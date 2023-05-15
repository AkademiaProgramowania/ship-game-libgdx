package ship.game.server.events;

public class EventBus {

    private static EventBusImplementation instance = new EventBusImplementation();

    public static void subscribe(EventType eventType, EventListener listener) {
        instance.subscribe(eventType, listener);
    }

    public static void unsubscribe(EventType eventType, EventListener listener) {
        instance.unsubscribe(eventType, listener);
    }

    public static void notify(Event event) {
        instance.notify(event);
    }
}
