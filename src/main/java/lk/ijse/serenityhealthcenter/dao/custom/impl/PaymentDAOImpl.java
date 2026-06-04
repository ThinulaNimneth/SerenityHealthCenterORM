package lk.ijse.serenityhealthcenter.dao.custom.impl;

import jakarta.persistence.NoResultException;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.PaymentDAO;
import lk.ijse.serenityhealthcenter.entity.Payment;
import lk.ijse.serenityhealthcenter.exception.PaymentException;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    FactoryConfiguration fc = FactoryConfiguration.getInstance();

    @Override
    public List<Payment> getAll() throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean save(Payment entity) throws SQLException, ClassNotFoundException {
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
    public boolean update(Payment entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Payment search(String sessionId) {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            Payment p = session.createQuery(
                            "FROM Payment WHERE therapySession.sessionId = :sid", Payment.class)
                    .setParameter("sid", Long.parseLong(sessionId)).getSingleResult();
            session.getTransaction().commit();
            return p;
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean pay(String paymentId, String payingAmount) {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            Payment payment = session.get(Payment.class, Long.parseLong(paymentId));
            if (payment == null) {
                throw new PaymentException("Payment record not found for ID: " + paymentId);
            }
            double amount = Double.parseDouble(payingAmount);
            if (amount <= 0) {
                throw new PaymentException("Payment amount must be greater than zero.");
            }
            payment.setRemainingAmount(payment.getRemainingAmount() - amount);
            session.update(payment);
            session.getTransaction().commit();
            return true;
        } catch (PaymentException e) {
            if (session.getTransaction() != null && session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive())
                session.getTransaction().rollback();
            throw new PaymentException("Payment processing failed: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Payment> getAllByPatient(String patientId) {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            List<Payment> list = session.createQuery(
                            "FROM Payment WHERE therapySession.patient.id = :pid", Payment.class)
                    .setParameter("pid", patientId).getResultList();
            session.getTransaction().commit();
            return list;
        } finally {
            session.close();
        }
    }
}