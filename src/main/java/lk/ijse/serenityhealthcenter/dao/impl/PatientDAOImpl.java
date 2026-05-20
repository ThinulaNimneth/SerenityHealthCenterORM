package lk.ijse.serenityhealthcenter.dao.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.PatientDAO;
import lk.ijse.serenityhealthcenter.entity.Patient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public  Long save(Patient patient){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try{
            Long id = (Long) session.save(patient);
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
    public  void update(Patient patient){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(patient);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }


    @Override
    public  void delete(Long id){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Patient patient = session.get(Patient.class, id);
            if (patient != null){
                session.remove(patient);
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
    public Optional<Patient> findById(Long id){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            Patient patient = session.get(Patient.class, id);
            return  Optional.ofNullable(patient);
        }finally {
            session.close();
        }
    }


    @Override
    public List<Patient> findAll(){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
         String hql = "FROM Patient";
            Query<Patient> query = session.createQuery(hql, Patient.class);
            return query.list();
        }finally {
            session.close();
        }
    }


    @Override
    public Optional<Patient> findByEmail(String email){
     Session session = FactoryConfiguration.getInstance().getSession().openSession();
     try {
         String hql = "FROM Patient p WHERE p.email = :email";
         Query<Patient> query = session.createQuery(hql, Patient.class);
         query.setParameter("email", email);
         return query.uniqueResultOptional();
     }finally {
         session.close();
     }
    }


    @Override
    public List<Patient> searchByName(String name){
     Session session = FactoryConfiguration.getInstance().getSession().openSession();
     try{
         String hql = "FROM Patient p WHERE p.name LIKE :name";
         Query<Patient> query = session.createQuery(hql, Patient.class);
         query.setParameter("name", "%" + name + "%");
         return query.list();
     }finally {
         session.close();
     }
    }


    @Override
    public List<Patient> findPatientsEnrolledInAllPrograms() {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            //  find patients whose enrolled program count equals total program count
            String hql = "SELECT p FROM Patient p " +
                    "WHERE SIZE(p.enrolledPrograms) = " +
                    "(SELECT COUNT(tp) FROM TherapyProgram tp WHERE tp.isActive = true)";

            Query<Patient> query = session.createQuery(hql, Patient.class);
            return query.list();
        } finally {
            session.close();
        }
    }

// Retrieve patient along with their enrolled therapy programs
    @Override
    public Optional<Patient> findPatientWithPrograms(Long patientId) {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "SELECT DISTINCT p FROM Patient p " +
                    "LEFT JOIN FETCH p.enrolledPrograms " +
                    "WHERE p.patientId = :patientId";

            Query<Patient> query = session.createQuery(hql, Patient.class);
            query.setParameter("patientId", patientId);
            return query.uniqueResultOptional();
        } finally {
            session.close();
        }
    }



}
