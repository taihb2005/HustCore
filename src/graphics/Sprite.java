package graphics;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Sprite {
    final private BufferedImage SPRITESHEET;
    private BufferedImage[][] spriteArray;

    public int w;
    public int h;

    final private int spriteCols;
    final private int spriteRows;

    public Sprite(String filepath)
    {
        this.w = 16 * 3; //original tile size
        this.h = 16 * 3; //original tile size

        SPRITESHEET = loadFile(filepath);

        spriteCols = SPRITESHEET.getWidth() / w;
        spriteRows = SPRITESHEET.getHeight() / h;

        loadSpriteArray();

    }

    public Sprite(String filepath , int w , int h)
    {
        this.w = w;
        this.h = h;

        SPRITESHEET = loadFile(filepath);

        spriteCols = SPRITESHEET.getWidth() / w; //get number of col of the spritesheet
        spriteRows = SPRITESHEET.getHeight() / h; // get the number of rows of the spritesheet

    }

    public BufferedImage loadFile(String filepath)
    {
        BufferedImage sprite = null;
        try{
            sprite = ImageIO.read(getClass().getResourceAsStream(filepath));
        } catch(Exception e)
        {
            System.out.println("Failed to load sprite: " + filepath);
        }
        return sprite;
    }

    public void loadSpriteArray()
    {
        spriteArray = new BufferedImage[spriteRows][spriteCols];
        for(int i = 0; i < spriteRows ; i++)
        {
            for(int j = 0 ; j < spriteCols ; j++)
                spriteArray[i][j] = getSprite(i , j);
        }
    }

    private BufferedImage getSprite(int x , int y)
    {
        return SPRITESHEET.getSubimage(y * h , x * w , w , h);
    }

    public BufferedImage getSpriteSheet(){return SPRITESHEET;};
    public BufferedImage[] getSpriteArrayRow(int i){return spriteArray[i];};
    public BufferedImage[][] getSpriteArray(){return spriteArray;};

    public int getSpriteCols(){return spriteCols;};
    public int getSpriteRows(){return spriteRows;};
}
