package lk.ijse.serenityhealthcenter.bo.custom.impl;

import lk.ijse.serenityhealthcenter.bo.custom.TherapistBO;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.DAOFactory;
import lk.ijse.serenityhealthcenter.dao.custom.ProgramDAO;
import lk.ijse.serenityhealthcenter.dao.custom.TherapistDAO;
import lk.ijse.serenityhealthcenter.dto.TherapistDto;
import lk.ijse.serenityhealthcenter.entity.Program;
import lk.ijse.serenityhealthcenter.entity.Therapist;
import lk.ijse.serenityhealthcenter.entity.TherapistProgram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TherapistBOImpl implements TherapistBO {
    TherapistDAO therapistDAO = (TherapistDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.THERAPIST);
    ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PROGRAM);

    @Override
    public ObservableList<TherapistDto> getAllTherapists() throws SQLException, ClassNotFoundException {
        List<Therapist> therapists = therapistDAO.getAll();
        List<TherapistDto> dtos = new ArrayList<>();
        for (Therapist t : therapists) {
            dtos.add(new TherapistDto(t.getTherapistId(), t.getName(),
                    t.getSpecialization(), t.getContactNo(), t.getStatus()));
        }
        return FXCollections.observableArrayList(dtos);
    }

    @Override
    public void addTherapist(TherapistDto dto, String programId) throws SQLException, ClassNotFoundException {
        Therapist therapist = new Therapist(dto.getTherapistId(), dto.getName(),
                dto.getSpecialization(), dto.getContactNo(), dto.getStatus());

        Session session = FactoryConfiguration.getInstance().getSession();
        session.beginTransaction();
        try {
            if (programId == null) {
                therapistDAO.save(therapist);
                session.getTransaction().commit();
            } else {
                Program program = programDAO.get(programId);
                TherapistProgram tp = new TherapistProgram(therapist, program);
                Serializable id = therapistDAO.saveTherapist(therapist, session);
                if (id != null) {
                    session.save(tp);
                    session.getTransaction().commit();
                }
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteTherapist(String id) throws SQLException, ClassNotFoundException {
        return therapistDAO.delete(id);
    }

    @Override
    public boolean updateTherapist(TherapistDto dto, String programId) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        try {
            Therapist therapist = new Therapist(dto.getTherapistId(), dto.getName(),
                    dto.getSpecialization(), dto.getContactNo(), dto.getStatus());

            if (programId == null) {
                therapistDAO.update(therapist);
                dto.setStatus("Available");
            } else {
                Program program = programDAO.get(programId);
                TherapistProgram tp = new TherapistProgram(therapist, program);
                therapistDAO.updateTherapist(therapist, session);
                session.update(tp);
                dto.setStatus("Not Available");
            }
            tx.commit();
            therapistDAO.updateStatus(dto);
            return true;
        } catch (Exception e) {
            tx.rollback();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public List<String> getAvailableTherapistIds() {
        return therapistDAO.getAvailableTherapists();
    }
}
