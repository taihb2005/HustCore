package level.event;

import java.util.function.BooleanSupplier;

public class Event {
    private final BooleanSupplier triggerCondition;
    private final Runnable action;
    private boolean triggered = false;
    private boolean triggerMultipleTime = false;

    public Event(BooleanSupplier triggerCondition, Runnable action) {
        this.triggerCondition = triggerCondition;
        this.action = action;
    }

    public Event(BooleanSupplier triggerCondition, Runnable action, boolean multipleTime) {
        this.triggerCondition = triggerCondition;
        this.action = action;
        this.triggerMultipleTime = multipleTime;
    }

    public void update() {
        if (!triggered && triggerCondition.getAsBoolean()) {
            action.run();
            triggered = true;
        }

        if(triggerMultipleTime && triggered && !triggerCondition.getAsBoolean()){
            triggered = false;
        }
    }

    public void reset() {
        triggered = false;
    }

    public boolean isTriggered() {
        return triggered;
    }
}
