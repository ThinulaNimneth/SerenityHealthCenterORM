package lk.ijse.serenityhealthcenter.dao.impl;

import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.TherapyProgramDAO;
import lk.ijse.serenityhealthcenter.entity.TherapyProgram;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TherapyProgramDAOImpl implements TherapyProgramDAO {

    @Override
    public  String save(TherapyProgram program){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(program);
            transaction.commit();
            return program.getProgramId();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }


    @Override
    public void update(TherapyProgram program){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.merge(program);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }


    @Override
    public void delete(String id){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            TherapyProgram program = session.get(TherapyProgram.class,id);
            if (program != null) session.remove(program);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            throw e;
        }finally {
            session.close();
        }
    }


    @Override
    public Optional<TherapyProgram> findById(String id){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            return Optional.ofNullable(session.get(TherapyProgram.class,id));
        }finally {
            session.close();
        }
    }

    @Override
    public List<TherapyProgram> findAll() {
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            return session.createQuery("FROM TherapyProgram", TherapyProgram.class).list();
        } finally {
            session.close();
        }
    }


    @Override
    public List<TherapyProgram> findActive(){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "FROM TherapyProgram tp WHERE tp.isActive = true";
            return  session.createQuery(hql , TherapyProgram.class).list();
        }finally {
            session.close();
        }
    }


    @Override
    public  long countAllPrograms(){
        Session session = FactoryConfiguration.getInstance().getSession().openSession();
        try {
            String hql = "SELECT COUNT(tp) FROM TherapyProgram tp WHERE tp.isActive = true";
            return session.createQuery(hql, long.class).uniqueResult();
        }finally {
            session.close();
        }
    }

}
