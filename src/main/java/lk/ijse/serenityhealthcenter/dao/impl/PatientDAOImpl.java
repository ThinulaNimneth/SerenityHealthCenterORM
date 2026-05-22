package lk.ijse.serenityhealthcenter.dao.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.PatientDAO;
import lk.ijse.serenityhealthcenter.entity.Patient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public Long save(Patient patient) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            transaction = session.beginTransaction();
            Long id = (Long) session.save(patient);
            transaction.commit();
            System.out.println("✅ Patient saved with ID: " + id);
            return id;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void update(Patient patient) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            transaction = session.beginTransaction();
            session.merge(patient);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            transaction = session.beginTransaction();
            Patient patient = session.get(Patient.class, id);
            if (patient != null) {
                session.remove(patient);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Optional<Patient> findById(Long id) {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            Patient patient = session.get(Patient.class, id);
            return Optional.ofNullable(patient);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Patient> findAll() {
        Session session = null;
        List<Patient> patients = new ArrayList<>();
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            // Simple query first - no JOIN FETCH to avoid complexity
            String hql = "FROM Patient";
            Query<Patient> query = session.createQuery(hql, Patient.class);
            patients = query.list();
            System.out.println("📊 DAO findAll() - Found " + patients.size() + " patients");

            // Debug: Print each patient
            for (Patient p : patients) {
                System.out.println("   Patient: ID=" + p.getPatientId() +
                        ", Name=" + p.getName() +
                        ", Email=" + p.getEmail());
            }

            return patients;
        } catch (Exception e) {
            System.err.println("Error in findAll(): " + e.getMessage());
            e.printStackTrace();
            return patients;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            String hql = "FROM Patient p WHERE p.email = :email";
            Query<Patient> query = session.createQuery(hql, Patient.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Patient> searchByName(String name) {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            String hql = "FROM Patient p WHERE p.name LIKE :name";
            Query<Patient> query = session.createQuery(hql, Patient.class);
            query.setParameter("name", "%" + name + "%");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Patient> findPatientsEnrolledInAllPrograms() {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            String hql = "SELECT p FROM Patient p " +
                    "WHERE SIZE(p.enrolledPrograms) = " +
                    "(SELECT COUNT(tp) FROM TherapyProgram tp WHERE tp.isActive = true)";
            Query<Patient> query = session.createQuery(hql, Patient.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Optional<Patient> findPatientWithPrograms(Long patientId) {
        Session session = null;
        try {
            session = FactoryConfiguration.getInstance().getSession().openSession();
            String hql = "SELECT DISTINCT p FROM Patient p " +
                    "LEFT JOIN FETCH p.enrolledPrograms " +
                    "WHERE p.patientId = :patientId";
            Query<Patient> query = session.createQuery(hql, Patient.class);
            query.setParameter("patientId", patientId);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null) session.close();
        }
    }
}