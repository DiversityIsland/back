package com.amr.project.dao.impl;

import com.amr.project.dao.abstracts.ReadWriteDAO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Repository
public abstract class ReadWriteDAOImpl<T, K> implements ReadWriteDAO<T, K> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> clazz;

    @SuppressWarnings("unchecked")
    public ReadWriteDAOImpl() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public void persist(Object e) {
        entityManager.persist(e);
    }

    @Override
    public void update(Object e) {
        entityManager.merge(e);
    }

    @Override
    public void delete(Object e) {
        entityManager.remove(e);
    }

    @Override
    public void deleteByKeyCascadeEnable(K id) {
        entityManager.remove(entityManager.find(clazz, id));
    }

    @Override
    public void deleteByKeyCascadeIgnore(K id) {
        entityManager.createQuery(
                "DELETE FROM " + clazz.getName() + " e WHERE e.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(K id) {
        long count = (long) entityManager.createQuery("SELECT COUNT(e) FROM " +
                clazz.getName() + " e WHERE e.id =: id")
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public T getByKey(K id) {
        return entityManager.find(clazz, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    @Override
    public T getByName(String name) {
        return (T) entityManager.createQuery("SELECT c from "+ clazz.getName()+" c where c.name = :name")
                .setParameter("name", name).getResultList().stream().findAny().orElse(null);
    }
}
