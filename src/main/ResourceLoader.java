package main;

import java.io.*;

public class ResourceLoader {
    public static Reader getReader(String filePath) throws Exception {
        File file = new File(filePath);

        if (file.exists()) {
            // File exists on the file system (development mode)
            return new FileReader(file);
        } else {
            // File is inside the .jar (runtime mode)
            InputStream inputStream = ResourceLoader.class.getResourceAsStream(filePath.startsWith("/") ? filePath : "/" + filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }
            return new InputStreamReader(inputStream);
        }
    }
}
