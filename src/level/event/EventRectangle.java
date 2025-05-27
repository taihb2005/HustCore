package level.event;

import entity.Entity;

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

    public EventRectangle(int x , int y , int width , int height , boolean oneTimeOnly){
        super(x , y , width , height);
        eventFinished = false;
        oneTimeOnlyEvent = oneTimeOnly;
    }

    public boolean isTriggered(Entity byEntity){
        try{
        int newSolidAreaX1 = (int)byEntity.position.x+ byEntity.solidArea1.x;
        int newSolidAreaY1 = (int)byEntity.position.y + byEntity.solidArea1.y;

        Rectangle tmp1 = new Rectangle(newSolidAreaX1 , newSolidAreaY1 , byEntity.solidArea1.width , byEntity.solidArea1.height);

            if (tmp1.intersects(this)) {
                System.out.println("Event triggered");
                if (oneTimeOnlyEvent) eventFinished = true;
                return true;
            }
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
