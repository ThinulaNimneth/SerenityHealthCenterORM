package lk.ijse.serenityhealthcenter.dao.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.UserDAO;
import lk.ijse.serenityhealthcenter.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public Long save(User user){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
                try{
                    Long id = (Long)  session.save(user);
                    transaction.commit();
                    return id;
                }catch (Exception e){
                    transaction.rollback();
                    throw e;
                }finally {
                    session.close();
                }
    }

    @Override
    public void update(User user) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(user);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    @Override
    public void  delete(Long id) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.get(User.class,id);
            if (user != null){
                session.remove(user);
            }
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }


    @Override
    public Optional<User> findById(Long id){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            User user = session.get(User.class,id);
            return  Optional.ofNullable(user);
        }finally {
            session.close();
        }
    }

    @Override
    public List<User> findAll() {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM User";
            Query<User> query = session.createQuery(hql, User.class);
            return query.list();
        } finally {
            session.close();
        }
    }


    @Override
    public Optional<User> findByUsername(String username){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM User u WHERE u.username = :username";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            return query.uniqueResultOptional();
        } finally {
            session.close();
        }
    }


    @Override
    public Optional<User> findByEmail(String email){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM User u WHERE u.email = :email";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        }finally {
            session.close();
        }
    }



    @Override
    public List<User> findByRole(User.UserRole role){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM User u WHERE u.role = :role";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("role", role);
            return query.list();
        }finally {
            session.close();
        }
    }




}
