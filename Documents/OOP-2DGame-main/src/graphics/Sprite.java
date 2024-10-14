package graphics;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

 /*
    This class uses for handle character sprite like running , attacking, dying,...
    all goes into one big png file which contains multiple sprite. It's convenient to
    animate later in the Animation class where we will dive further into behaviors of
    characters.
 */

public class Sprite {
    final private BufferedImage SPRITESHEET;
    private BufferedImage[][] spriteArray;

    public int w;
    public int h;

    final private int spriteCols;
    final private int spriteRows;

    public Sprite(String filepath) //Instantiate a sprite from a certain location
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

        spriteCols = SPRITESHEET.getWidth() / w; //get number of cols of the sprite sheet
        spriteRows = SPRITESHEET.getHeight() / h; // get the number of rows of the sprite sheet

    }

    public BufferedImage loadFile(String filepath)  //load the sprite
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

    public void loadSpriteArray() //load the sprite into 2-dimensional arrays
    {
        spriteArray = new BufferedImage[spriteRows][spriteCols];
        for(int i = 0; i < spriteRows ; i++)
        {
            for(int j = 0 ; j < spriteCols ; j++)
                spriteArray[i][j] = getSprite(i , j);
        }
    }

    private BufferedImage getSprite(int x , int y) //get sub image from the sprite, at the position x , y from the img
    {
        return SPRITESHEET.getSubimage(y * h , x * w , w , h);
    }


    //setter and getter method
    public BufferedImage getSpriteSheet(){return SPRITESHEET;};
    public BufferedImage[] getSpriteArrayRow(int i){return spriteArray[i];};
    public BufferedImage[][] getSpriteArray(){return spriteArray;};

    public int getSpriteCols(){return spriteCols;};
    public int getSpriteRows(){return spriteRows;};
}
