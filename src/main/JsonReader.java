package main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.GameObject;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonReader {
    public static void main(String[] args) {
        try {
            // Đọc file JSON
            Reader reader = new FileReader("res/entity/object.json");

            // Sử dụng Gson để ánh xạ
            Gson gson = new Gson();

            // Đọc dữ liệu vào Map
            Map<String, List<GameObject>> data = gson.fromJson(reader, new TypeToken<Map<String, List<GameObject>>>() {}.getType());

            // Lấy danh sách active và inactive
            List<GameObject> activeObjects = data.get("active");
            List<GameObject> inactiveObjects = data.get("inactive");

            // Hiển thị thông tin các object
            for (GameObject obj : inactiveObjects) {
                System.out.println("Name: " + obj.getName());
                System.out.println("Object: " + obj.getObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
