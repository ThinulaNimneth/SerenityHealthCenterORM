package lk.ijse.serenityhealthcenter.dao.custom.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.TherapistDAO;
import lk.ijse.serenityhealthcenter.dto.TherapistDto;
import lk.ijse.serenityhealthcenter.entity.Therapist;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class TherapistDAOImpl implements TherapistDAO{
    FactoryConfiguration fc = FactoryConfiguration.getInstance();

    @Override
    public List<Therapist> getAll() throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            return session.createQuery("FROM Therapist", Therapist.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean save(Therapist entity) throws SQLException, ClassNotFoundException {
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
    public boolean update(Therapist entity) throws SQLException, ClassNotFoundException {
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
            Therapist t = session.get(Therapist.class, id);
            if (t != null) session.delete(t);
            session.getTransaction().commit();
            return true;
        } finally {
            session.close();
        }
    }

    @Override
    public Serializable saveTherapist(Therapist therapist, Session session) {
        return (Serializable) session.save(therapist);
    }

    @Override
    public List<String> getAvailableTherapists() {
        Session session = fc.getSession();
        try {
            return session.createQuery("FROM Therapist WHERE status = 'Available'", Therapist.class)
                    .list().stream().map(Therapist::getTherapistId).toList();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateStatus(TherapistDto therapistDto) {
        Session session = fc.getSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("UPDATE Therapist SET status = :status WHERE therapistId = :id");
            query.setParameter("status", therapistDto.getStatus());
            query.setParameter("id", therapistDto.getTherapistId());
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Serializable updateTherapist(Therapist therapist, Session session) {
        session.update(therapist);
        return therapist.getTherapistId();
    }
}
