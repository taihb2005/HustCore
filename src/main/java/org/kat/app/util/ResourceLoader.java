package org.kat.app.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ResourceLoader {
    public static Reader getReader(String filePath) throws Exception {
        File file = new File(filePath);

        if (file.exists()) {
            return new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        } else {
            InputStream inputStream = ResourceLoader.class.getResourceAsStream(filePath.startsWith("/") ? filePath : "/" + filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + filePath);
            }
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        }
    }
}
