package level;

import entity.Entity;

import java.util.HashMap;

public class EntityManager {
    private final HashMap<String, Entity> entityPool = new HashMap<>();

    public void add(String nameId, Entity e){
        entityPool.put(nameId, e);
    }

    public void remove(String nameId){
        entityPool.remove(nameId);
    }

    public void clear(){
        entityPool.clear();
    }

    public <T extends Entity> T get(String nameId, Class<T> clazz) {
        Entity entity = entityPool.get(nameId);
        if (entity == null) {
            return null;
        }
        if (!clazz.isInstance(entity)) {
            return null;
        }
        return clazz.cast(entity);
    }


}
