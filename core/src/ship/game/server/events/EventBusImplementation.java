package ship.game.server.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBusImplementation {

    private final Map<EventType, List<EventListener>> allListeners;

    public EventBusImplementation() {
        allListeners = new HashMap<>();
        for (EventType eventType : EventType.values()) {
            allListeners.put(eventType, new ArrayList<EventListener>());
        }
    }

    public void subscribe(EventType eventType, EventListener listener) {
        allListeners.get(eventType).add(listener);
    }

    public void unsubscribe(EventType eventType, EventListener listener) {
        allListeners.get(eventType).remove(listener);
    }

    public void notify(Event event) {
        List<EventListener> eventListeners = allListeners.get(event.getType());
        for (EventListener listener : eventListeners) {
            listener.react(event);
        }
    }
}
