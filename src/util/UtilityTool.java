package util;

import java.awt.Rectangle;

public class UtilityTool {


    public static Rectangle scaleHitbox(Rectangle rect , int scale)
    {
        return new Rectangle(rect.x * scale , rect.y * scale  , rect.width * scale , rect.height * scale);
    }
}
