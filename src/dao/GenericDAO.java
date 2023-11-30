/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.*;
import org.eclipse.persistence.sessions.Session;



/**
 *
 * @author Moreno
 */
public class GenericDAO<T> {

    private final EntityManager entityManager;
    private final Class persistentClass;

    public GenericDAO() {
        this.entityManager = ManagerFactory.getEntityManager();
        this.persistentClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void inserir(T entity) {
        EntityTransaction tx = getEntityManager().getTransaction();

        try {
            tx.begin();
            getEntityManager().persist(entity);
            tx.commit();
        } catch (Throwable t) {
            t.printStackTrace();
            tx.rollback();
        } finally {
            close();
        }
    }

    public void editar(T entity) {
        EntityTransaction tx = getEntityManager().getTransaction();

        try {
            tx.begin();
            getEntityManager().merge(entity);
            tx.commit();
        } catch (Throwable t) {
            t.printStackTrace();
            tx.rollback();
        } finally {
            close();
        }

    }

    public void excluir(T entity) {
        EntityTransaction tx = getEntityManager().getTransaction();

        try {
            tx.begin();
            getEntityManager().remove(entity);
            tx.commit();
        } catch (Throwable t) {
            t.printStackTrace();
            tx.rollback();
        } finally {
            close();
        }
    }

    public List selecionarTodos() throws Exception {
        return getEntityManager().createNamedQuery(persistentClass.getSimpleName() + ".findAll")
                .getResultList();
    }

    public T selecionarPorCodigo(long codigo) {
        return (T) getEntityManager().createNamedQuery(persistentClass.getSimpleName() + ".findByCodigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
    }

    private void close() {
        if (getEntityManager().isOpen()) {
            getEntityManager().close();
        }
    }
}
