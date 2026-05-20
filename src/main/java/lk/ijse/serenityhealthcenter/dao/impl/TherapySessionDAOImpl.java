package lk.ijse.serenityhealthcenter.dao.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.TherapySessionDAO;
import lk.ijse.serenityhealthcenter.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TherapySessionDAOImpl implements TherapySessionDAO {

    @Override
    public Long save(TherapySession session){
        Session hibernateSession = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = hibernateSession.beginTransaction();
        try {
            Long id = (Long) hibernateSession.save(session);
            transaction.commit();
            return id;
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            hibernateSession.close();
        }
    }


    @Override
    public void update(TherapySession session){
        Session hibernateSession = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = hibernateSession.beginTransaction();
        try {
            hibernateSession.merge(session);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            hibernateSession.close();
        }
    }


    @Override
    public void delete(Long id){
        Session hibernateSession = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = hibernateSession.beginTransaction();
        try {
            TherapySession session = hibernateSession.get(TherapySession.class, id);
            if (session != null) hibernateSession.remove(session);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            hibernateSession.close();
        }
    }


    @Override
    public Optional<TherapySession> findById(Long id){
        Session hibernateSession = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            return Optional.ofNullable(hibernateSession.get(TherapySession.class, id));
        }finally {
            hibernateSession.close();
        }
    }

    @Override
    public List<TherapySession> findAll() {
        Session hibernateSession = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            return hibernateSession.createQuery("FROM TherapySession", TherapySession.class).list();
        } finally {
            hibernateSession.close();
        }
    }

    @Override
    public List<TherapySession> findByPatient(Long patientId) {
        Session hibernateSession = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM TherapySession ts WHERE ts.patient.patientId = :patientId";
            Query<TherapySession> query = hibernateSession.createQuery(hql, TherapySession.class);
            query.setParameter("patientId", patientId);
            return  query.list();
        }finally {
            hibernateSession.close();
        }
    }


    @Override
    public List<TherapySession> findByTherapist(Long therapistId) {
        Session hibernateSession = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM TherapySession ts WHERE ts.therapist.therapistId = :therapistId";
            Query<TherapySession> query =  hibernateSession.createQuery(hql, TherapySession.class);
            query.setParameter("therapistId", therapistId);
            return   query.list();
        }finally {
            hibernateSession.close();
        }
    }





}
