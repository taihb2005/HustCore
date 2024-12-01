package level;

import java.awt.*;

public class EventRectangle extends Rectangle {
    public boolean eventFinished;
    public boolean oneTimeOnlyEvent;
    public EventRectangle(){
        super();
        eventFinished = false;
        oneTimeOnlyEvent = false;
    }

    public EventRectangle(int x , int y , int width , int height){
        super(x , y , width , height);
        eventFinished = false;
        oneTimeOnlyEvent = false;
    }
}
