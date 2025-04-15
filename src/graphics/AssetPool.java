package graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Objects;

public class AssetPool {
    private static final Path spriteRootFolder = Paths.get("res");

    private static HashMap<String, BufferedImage> _assetPool= new HashMap<>();
    public static final HashMap<String, BufferedImage> assetPool = _assetPool;

    public void updateSpritePool(){

    }

    public static void loadAll() throws IOException {
        Files.walkFileTree(spriteRootFolder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {

                String spriteName = filePath.getFileName().toString();

                if (spriteName.endsWith(".png")) {
                    Path relativePath = spriteRootFolder.relativize(filePath);

                    String resourcePath = "/" + relativePath.toString().replace("\\", "/");

                    BufferedImage asset = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));

                    _assetPool.put(spriteName, asset);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static BufferedImage getImage(String name){
        return assetPool.get(name);
    }

}
