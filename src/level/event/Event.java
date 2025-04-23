package level.event;

import java.util.function.BooleanSupplier;

public class Event {
    private final BooleanSupplier triggerCondition;
    private final Runnable action;
    private boolean triggered = false;

    public Event(BooleanSupplier triggerCondition, Runnable action) {
        this.triggerCondition = triggerCondition;
        this.action = action;
    }

    public void update() {
        if (!triggered && triggerCondition.getAsBoolean()) {
            action.run();
            triggered = true;
        }
    }

    public void reset() {
        triggered = false;
    }

    public boolean isTriggered() {
        return triggered;
    }
}
