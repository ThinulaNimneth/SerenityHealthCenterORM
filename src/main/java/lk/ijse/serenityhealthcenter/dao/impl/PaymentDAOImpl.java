package lk.ijse.serenityhealthcenter.dao.impl;

import jakarta.persistence.criteria.From;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.PaymentDAO;
import lk.ijse.serenityhealthcenter.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public Long save(Payment payment) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long id = (Long)  session.save(payment);
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
    public void update(Payment payment) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(payment);
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
            Payment payment = session.get(Payment.class, id);
            if (payment != null) session.remove(payment);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }


    @Override
    public Optional<Payment> findById(Long id){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            return Optional.ofNullable(session.get(Payment.class, id));
        }finally {
            session.close();
        }
    }

    @Override
    public List<Payment> findAll() {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            return session.createQuery("FROM Payment", Payment.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Payment> findByPatient(Long patientId) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM Payment p WHERE p.patient.patientId = :patientId";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("patientId", patientId);
            return query.list();
        } finally {
            session.close();
        }
    }


    @Override
    public List<Payment> findByDateRange(LocalDate start, LocalDate end){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql =  "FROM Payment p WHERE p.paymentDate BETWEEN :start AND :end";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("start", start);
            query.setParameter("end", end);
            return  query.list();
        }finally {
            session.close();
        }
    }

    @Override
    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM Payment p WHERE p.status = :status";
            Query<Payment> query = session.createQuery(hql, Payment.class);
            query.setParameter("status", status);
            return  query.list();
        }finally {
            session.close();
        }
    }



}
