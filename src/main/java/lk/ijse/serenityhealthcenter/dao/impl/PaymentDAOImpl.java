package lk.ijse.serenityhealthcenter.dao.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.PaymentDAO;
import lk.ijse.serenityhealthcenter.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

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





}
