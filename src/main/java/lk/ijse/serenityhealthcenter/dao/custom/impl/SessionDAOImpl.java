package lk.ijse.serenityhealthcenter.dao.custom.impl;

import jakarta.persistence.NoResultException;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.SessionDAO;
import lk.ijse.serenityhealthcenter.entity.Program;
import lk.ijse.serenityhealthcenter.entity.Therapist;
import lk.ijse.serenityhealthcenter.entity.TherapySession;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

public class SessionDAOImpl implements SessionDAO {
    FactoryConfiguration fc = FactoryConfiguration.getInstance();

    @Override
    public List<TherapySession> getAll() throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            return session.createQuery("FROM TherapySession", TherapySession.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean save(TherapySession entity) throws SQLException, ClassNotFoundException {
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
    public boolean update(TherapySession entity) throws SQLException, ClassNotFoundException {
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
    public boolean delete(String patientId) throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            int count = session.createQuery("DELETE FROM TherapySession WHERE patient.id = :pid")
                    .setParameter("pid", patientId).executeUpdate();
            session.getTransaction().commit();
            return count > 0;
        } finally {
            session.close();
        }
    }

    @Override
    public String search(String programId) {
        Session session = fc.getSession();
        try {
            Program program = session.get(Program.class, programId);
            if (program == null) return null;
            return session.createQuery(
                            "SELECT tp.therapist FROM TherapistProgram tp WHERE tp.program = :program", Therapist.class)
                    .setParameter("program", program).getSingleResult().getTherapistId();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public List<String> getProgramIds(String patientId) {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            List<String> list = session.createQuery(
                            "SELECT program.programId FROM TherapySession WHERE patient.id = :pid", String.class)
                    .setParameter("pid", patientId).list();
            session.getTransaction().commit();
            return list;
        } finally {
            session.close();
        }
    }

    @Override
    public Long searchSessionId(String patientId, String programId) {
        Session session = fc.getSession();
        try {
            session.beginTransaction();
            Long id = session.createQuery(
                            "SELECT sessionId FROM TherapySession WHERE patient.id = :pid AND program.programId = :progId", Long.class)
                    .setParameter("pid", patientId).setParameter("progId", programId).getSingleResult();
            session.getTransaction().commit();
            return id;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Object[]> getPatientTherapyHistory(String patientId) {
        Session session = fc.getSession();
        try {
            String hql = "SELECT p.programId, p.name, t.name, p.fee, pay.remainingAmount, ts.sessionDate " +
                    "FROM TherapySession ts " +
                    "JOIN ts.program p " +
                    "JOIN ts.therapist t " +
                    "LEFT JOIN Payment pay ON pay.therapySession = ts " +
                    "WHERE ts.patient.id = :pid " +
                    "ORDER BY ts.sessionDate DESC";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("pid", patientId);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    @Override
    public List<String> getPatientIdsFromTherapySessions() {
        Session session = fc.getSession();
        try {
            return session.createQuery("SELECT DISTINCT patient.id FROM TherapySession", String.class).list();
        } finally {
            session.close();
        }
    }
}
