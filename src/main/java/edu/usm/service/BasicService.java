package edu.usm.service;

import edu.usm.domain.BasicEntity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by scottkimball on 4/11/15.
 */
public abstract class BasicService {

    protected void updateLastModified ( BasicEntity entity) {
        entity.setLastModified(LocalDateTime.now().toString());

    }

    protected void emptyStringToNull (BasicEntity entity) {

        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Class clazz = field.getType();

                if (clazz == String.class) {
                    String s = (String) field.get(entity);
                    if (s != null && s.equals("")) {
                        field.set(entity, null);
                    }
                }
            }

        } catch (IllegalAccessException e) { }
    }

    protected void updateLastModifiedCollection ( List<? extends BasicEntity> entities) {
        entities.stream().forEach(e -> e.setLastModified(LocalDateTime.now().toString()));
    }
}
