package level.event;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    private final List<Event> events = new ArrayList<>();

    public void register(Event event) {
        events.add(event);
    }

    public void update() {
        for (Event event : events) {
            if(!event.isTriggered()) event.update();
        }
    }

    public void resetAll() {
        for (Event event : events) {
            event.reset();
        }
    }

    public void clear() {
        events.clear();
    }
}
