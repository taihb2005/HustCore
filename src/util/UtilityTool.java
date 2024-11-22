package util;

import java.awt.Rectangle;

public class UtilityTool {

    public static Rectangle scaleHitbox(Rectangle rect , int scale)
    {
        return new Rectangle(rect.x * scale , rect.y * scale  , rect.width * scale , rect.height * scale);
    }

    public static double distance(int x1 , int y1 , int x2 , int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


}
