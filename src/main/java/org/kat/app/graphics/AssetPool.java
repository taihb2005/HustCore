package org.kat.app.graphics;

import org.jetbrains.annotations.NotNull;
import org.kat.app.util.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

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
        InputStream pathListStream = AssetPool.class.getResourceAsStream("/data/path/resources_path.txt");
        if (pathListStream == null) {
            throw new FileNotFoundException("Không tìm thấy file resources_path.txt trong /data/");
        }

        List<String> existingPaths;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(pathListStream))) {
            existingPaths = reader.lines().toList();
        }

        if (UtilityTool.isJar()) {
            for (String relative : existingPaths) {
                String resourcePath = "/textures/" + relative;
                InputStream is = AssetPool.class.getResourceAsStream(resourcePath);
                if (is == null) {
                    System.err.println("Không tìm thấy: " + resourcePath);
                    continue;
                }

                BufferedImage asset = ImageIO.read(is);
                String spriteName = Paths.get(relative).getFileName().toString();
                _assetPool.put(spriteName, asset);
            }
        } else {
            Set<String> pathSet = new HashSet<>(existingPaths);
            List<String> newPaths = new ArrayList<>();

            Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {
                @Override
                public @NotNull FileVisitResult visitFile(@NotNull Path filePath, @NotNull BasicFileAttributes attrs) throws IOException {
                    String spriteName = filePath.getFileName().toString();

                    if (spriteName.endsWith(".png")) {
                        Path relativePath = SPRITE_FOLDER.relativize(filePath);
                        String relativePathStr = relativePath.toString().replace("\\", "/");

                        String resourcePath = "/textures/" + relativePathStr;

                        InputStream is = AssetPool.class.getResourceAsStream(resourcePath);
                        if (is == null) {
                            throw new FileNotFoundException("Không tìm thấy resource: " + resourcePath);
                        }

                        BufferedImage asset = ImageIO.read(is);
                        _assetPool.put(spriteName, asset);

                        if (!pathSet.contains(relativePathStr)) {
                            newPaths.add(relativePathStr);
                        }
                    }

                    return FileVisitResult.CONTINUE;
                }
            });

            if (!newPaths.isEmpty()) {
                Path resourceListPath = Paths.get("src/main/resources/data/resources_path.txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(resourceListPath.toFile(), true))) {
                    for (String line : newPaths) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        }
    }


    public static BufferedImage getImage(String name){
        return assetPool.get(name);
    }

    public static void dispose(){
        _assetPool.clear();
    }

}
