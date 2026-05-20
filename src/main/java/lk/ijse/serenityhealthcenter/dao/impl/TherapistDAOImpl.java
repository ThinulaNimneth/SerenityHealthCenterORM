package lk.ijse.serenityhealthcenter.dao.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.TherapistDAO;
import lk.ijse.serenityhealthcenter.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;


public class TherapistDAOImpl implements TherapistDAO {

    @Override
    public  Long save (Therapist therapist) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Long id = (Long)  session.save(therapist);
            transaction.commit();
            return id;
        }catch (Exception e){
            transaction.rollback();
            throw e ;
        }finally {
            session.close();
        }
    }

    @Override
    public void update(Therapist therapist) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(therapist);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }


    @Override
    public void delete(Long id){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Therapist therapist = session.get(Therapist.class,id);
            if (therapist != null) session.remove(therapist);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    @Override
    public Optional<Therapist> findById(Long id) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            return Optional.ofNullable(session.get(Therapist.class, id));
        }finally {
            session.close();
        }
    }


    @Override
    public List<Therapist> findAll() {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            return session.createQuery("FROM Therapist ", Therapist.class).list();
        }finally {
            session.close();
        }
    }

    @Override
    public List<Therapist> findAvailable() {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM Therapist t WHERE t.isAvailable = true";
            return  session.createQuery(hql, Therapist.class).list();
        }finally {
            session.close();
        }
    }


    @Override
    public List<Therapist> findBySpecialization(String specialization){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try{
            String hql = "FROM Therapist t WHERE t.specialization = :spec";
            Query<Therapist> query = session.createQuery(hql, Therapist.class);
            query.setParameter("spec", specialization);
            return query.list();
        }finally {
            session.close();
        }
    }


    @Override
    public Optional<Therapist> findByEmail(String email) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM Therapist t WHERE t.email = :email";
            Query<Therapist> query = session.createQuery(hql, Therapist.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        }finally {
            session.close();
        }
    }



}
