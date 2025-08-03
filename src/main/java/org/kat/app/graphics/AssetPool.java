package org.kat.app.graphics;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

public class AssetPool {
    public static Path SPRITE_FOLDER = Paths.get("src", "main", "resources", "textures");
    public static Path UI_FOLDER = Paths.get("src", "main", "resources", "textures", "ui");

    private static final HashMap<String, BufferedImage> _assetPool= new HashMap<>();
    public static final HashMap<String, BufferedImage> assetPool = _assetPool;

    static{
        if(!Files.exists(SPRITE_FOLDER)){
            SPRITE_FOLDER = Paths.get("textures");
        }
    }

    public static void loadAll(Path folderPath) throws IOException {
        Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {
            @Override
            public @NotNull FileVisitResult visitFile(@NotNull Path filePath, @NotNull BasicFileAttributes attrs) throws IOException {

                String spriteName = filePath.getFileName().toString();

                if (spriteName.endsWith(".png")) {
                    Path relativePath = SPRITE_FOLDER.relativize(filePath);

                    String resourcePath = "/textures/" + relativePath.toString().replace("\\", "/");

                    InputStream is = AssetPool.class.getResourceAsStream(resourcePath);
                    if (is == null) {
                        throw new FileNotFoundException("Resource not found: " + resourcePath);
                    }

                    BufferedImage asset = ImageIO.read(is);

                    _assetPool.put(spriteName, asset);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static BufferedImage getImage(String name){
        return assetPool.get(name);
    }

    public static void dispose(){
        _assetPool.clear();
    }

}
