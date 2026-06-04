package lk.ijse.serenityhealthcenter.dao.custom.impl;

import jakarta.persistence.NoResultException;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.custom.ProgramDAO;
import lk.ijse.serenityhealthcenter.entity.Program;
import org.hibernate.Session;

import java.sql.SQLException;
import java.util.List;

public class ProgramDAOImpl implements ProgramDAO {
    FactoryConfiguration fc = FactoryConfiguration.getInstance();

    @Override
    public List<Program> getAll() throws SQLException, ClassNotFoundException {
        Session session = fc.getSession();
        try {
            return session.createQuery("FROM Program", Program.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean save(Program entity) throws SQLException, ClassNotFoundException {
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
    public boolean update(Program entity) throws SQLException, ClassNotFoundException {
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
            Program p = session.get(Program.class, id);
            if (p != null) session.delete(p);
            session.getTransaction().commit();
            return true;
        } finally {
            session.close();
        }
    }

    @Override
    public Program get(String programId) {
        Session session = fc.getSession();
        try {
            return session.get(Program.class, programId);
        } finally {
            session.close();
        }
    }

    @Override
    public Program search(String programId) {
        Session session = fc.getSession();
        try {
            return session.createQuery("FROM Program WHERE programId = :pid", Program.class)
                    .setParameter("pid", programId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }
    }
}
