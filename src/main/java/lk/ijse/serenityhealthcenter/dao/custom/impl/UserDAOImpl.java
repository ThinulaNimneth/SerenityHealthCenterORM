package lk.ijse.serenityhealthcenter.dao.custom.impl;

import jakarta.persistence.NoResultException;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.UserDAO;
import lk.ijse.serenityhealthcenter.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    FactoryConfiguration fc = FactoryConfiguration.getInstance();

    @Override
    public boolean registerUser(User user) {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    @Override
    public User loginUser(String username) {
        Session session = fc.getSession();
        try {
            String hql = "FROM User WHERE username = :username";
            return (User) session.createQuery(hql).setParameter("username", username).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean ifHaveAdmin() {
        Session session = fc.getSession();
        try {
            String hql = "FROM User WHERE role = 'admin'";
            return !session.createQuery(hql).list().isEmpty();
        } finally {
            session.close();
        }
    }

    @Override
    public User search(String id) {
        Session session = fc.getSession();
        try {
            String hql = "FROM User WHERE id = :id";
            return session.createQuery(hql, User.class).setParameter("id", Long.parseLong(id)).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAll() throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            return session.createQuery("FROM User", User.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean save(User entity) throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
            return true;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(User entity) throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
            return true;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            User user = session.get(User.class, Long.parseLong(id));
            if (user != null) session.delete(user);
            session.getTransaction().commit();
            return true;
        } finally {
            session.close();
        }
    }
}