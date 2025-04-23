package util;

public class GameTimer {
    private int timeCounter = 0;
    private int lastCounter = 0;
    private int delayCounter = 0;
    private boolean loop = true;
    private int MAX_DELAY;
    private int MAX_TIME;

    private boolean paused;
    private boolean finished;

    private final Runnable onTimePassed;

    public GameTimer(Runnable onTimePassed, int delay, int period){
        this.MAX_TIME = period;
        this.MAX_DELAY = delay;
        this.onTimePassed = onTimePassed;
    }

    public GameTimer(Runnable onTimePassed, int period, boolean loop){
        this.MAX_TIME = period;
        this.MAX_DELAY = 0;
        this.loop = loop;
        this.onTimePassed = onTimePassed;
    }

    public GameTimer(Runnable onTimePassed, int period){
        this.MAX_TIME = period;
        this.MAX_DELAY = 0;
        this.onTimePassed = onTimePassed;
    }

    public GameTimer(Runnable onTimePassed){
        this.onTimePassed = onTimePassed;
        this.MAX_DELAY = 0;
        this.MAX_TIME = 0;
    }

    public GameTimer(Runnable onTimePassed, boolean loop){
        this.onTimePassed = onTimePassed;
        this.MAX_DELAY = 0;
        this.MAX_TIME = 0;
        this.loop = loop;
    }

    public void update(){
        if(delayCounter < MAX_DELAY){
            delayCounter++;
            return;
        }

        if(finished || paused) return;

        timeCounter++;
        if(timeCounter > MAX_TIME){
            if(!loop) {
                if (onTimePassed != null) onTimePassed.run();
                finished = true;
            } else {
                timeCounter = 0;
                if (onTimePassed != null) onTimePassed.run();
            }
        }
    }

    public void reset(){
        timeCounter = 0;
        delayCounter = 0;
        lastCounter = 0;
    }

    public void pause(){
        paused = true;
        lastCounter = timeCounter;
    }

    public void resume(){
        paused = false;
        timeCounter = lastCounter;
    }

    public void activate(){
        finished = false;
        if(onTimePassed != null) onTimePassed.run();
    }

    public void execute(){
        if(onTimePassed != null) onTimePassed.run();
    }

    public void setPeriod(int newPeriod){
        this.MAX_TIME = newPeriod;
    }

    public void setDelay(int newDelay){
        this.MAX_DELAY = newDelay;
    }

    public boolean isFinished(){
        return finished;
    }
}
