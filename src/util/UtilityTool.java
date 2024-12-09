package util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {

    public static Rectangle scaleHitbox(Rectangle rect , int scale)
    {
        return new Rectangle(rect.x * scale , rect.y * scale  , rect.width * scale , rect.height * scale);
    }

    public static double distance(int x1 , int y1 , int x2 , int y2){
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static BufferedImage scaleImage(BufferedImage original, int width, int height)
    {
        BufferedImage scaledImage = new BufferedImage(width,height,original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original,0,0,width,height,null);
        g2.dispose();
        return scaledImage;
    }
}
