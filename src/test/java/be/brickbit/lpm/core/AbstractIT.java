package be.brickbit.lpm.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractIT {
    @Autowired
    private EntityManager entityManager;

    protected void insert(Object... entities) throws Exception{
        List<Object> entitiesToSave = new ArrayList<>();

        for (Object entity : entities) {
            entitiesToSave.addAll(getDependencies(entity));
        }

        Collections.reverse(entitiesToSave);

        for (Object entity : entitiesToSave) {
            save(entity);
        }
    }

    private List<Object> getDependencies(Object entity) throws IllegalAccessException {
        List<Object> entities = new ArrayList<>();
        entities.add(entity);

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(OneToMany.class) ||
                    field.isAnnotationPresent(OneToOne.class) ||
                    field.isAnnotationPresent(ManyToOne.class)) {

                field.setAccessible(true);
                Object dependency = field.get(entity);

                if (dependency != null) {
                    entities.addAll(getDependencies(field.get(entity)));
                }
            } else if (field.isAnnotationPresent(ManyToMany.class)) {
                field.setAccessible(true);
                Iterable dependencies = (Iterable) field.get(entity);

                for (Object dependency : dependencies) {
                    entities.addAll(getDependencies(dependency));
                }
            }
        }

        return entities;
    }

    private void save(Object object) {
        if (entityManager.contains(object)) {
            entityManager.merge(object);
        } else {
            entityManager.persist(object);
        }
    }

    protected EntityManager getEntityManager(){
        return entityManager;
    }
}
