package lk.ijse.serenityhealthcenter.dao.custom.impl;

import jakarta.persistence.NoResultException;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.PatientDAO;
import lk.ijse.serenityhealthcenter.entity.Patient;
import lk.ijse.serenityhealthcenter.exception.RegistrationException;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    FactoryConfiguration fc = FactoryConfiguration.getInstance();

    @Override
    public List<Patient> getAll() throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            return session.createQuery("FROM Patient", Patient.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean save(Patient entity) throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            // Check for duplicate patient ID
            Patient existing = session.get(Patient.class, entity.getId());
            if (existing != null) {
                throw new RegistrationException("Patient ID '" + entity.getId() + "' already exists. Duplicate entries are not allowed.");
            }
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
            return true;
        } catch (RegistrationException e) {
            throw e;
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new RegistrationException("Failed to register patient: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Patient entity) throws SQLException, ClassNotFoundException {
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
            Patient patient = session.get(Patient.class, id);
            if (patient != null) session.delete(patient);
            session.getTransaction().commit();
            return true;
        } finally {
            session.close();
        }
    }

    @Override
    public Patient search(String id) {
        Session session = fc.getSession();
        try {
            return session.createQuery("FROM Patient WHERE id = :id", Patient.class)
                    .setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }

   // A patient qualifies if there does NOT EXIST any Program for which they do NOT have a TherapySession.
    @Override
    public List<Patient> getPatientsEnrolledInAllPrograms() {
        Session session = fc.getSession();
        try {
            String hql =
                    "FROM Patient p " +
                            "WHERE NOT EXISTS (" +
                            "    FROM Program prog " +
                            "    WHERE NOT EXISTS (" +
                            "        FROM TherapySession ts " +
                            "        WHERE ts.patient = p AND ts.program = prog" +
                            "    )" +
                            ")";
            return session.createQuery(hql, Patient.class).getResultList();
        } finally {
            session.close();
        }
    }

    // Returns Object[] rows: [Patient, Program] for each enrollment.
    @Override
    public List<Object[]> getPatientsWithTherapyPrograms() {
        Session session = fc.getSession();
        try {
            String hql =
                    "SELECT p, prog " +
                            "FROM Patient p " +
                            "JOIN TherapySession ts ON ts.patient = p " +
                            "JOIN ts.program prog " +
                            "ORDER BY p.id, prog.programId";
            return session.createQuery(hql, Object[].class).getResultList();
        } finally {
            session.close();
        }
    }
}