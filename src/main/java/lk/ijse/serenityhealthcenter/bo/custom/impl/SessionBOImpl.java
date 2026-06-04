package lk.ijse.serenityhealthcenter.bo.custom.impl;

import lk.ijse.serenityhealthcenter.bo.custom.SessionBO;
import lk.ijse.serenityhealthcenter.config.FactoryConfiguration;
import lk.ijse.serenityhealthcenter.dao.DAOFactory;
import lk.ijse.serenityhealthcenter.dao.custom.SessionDAO;
import lk.ijse.serenityhealthcenter.dto.PaymentDto;
import lk.ijse.serenityhealthcenter.dto.TherapyProgramHistoryDto;
import lk.ijse.serenityhealthcenter.dto.TherapySessionDto;
import lk.ijse.serenityhealthcenter.entity.*;
import lk.ijse.serenityhealthcenter.exception.PaymentException;
import lk.ijse.serenityhealthcenter.exception.SchedulingException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SessionBOImpl implements SessionBO {
    SessionDAO sessionDAO = (SessionDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SESSION);

    @Override
    public boolean addSession(TherapySessionDto sessionDto, PaymentDto paymentDto) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Scheduling conflict check — patient already enrolled in this program
            List<String> existingPrograms = sessionDAO.getProgramIds(sessionDto.getPatientId());
            if (existingPrograms.contains(sessionDto.getProgramId())) {
                throw new SchedulingException("Scheduling conflict: Patient '" + sessionDto.getPatientId() +
                        "' is already enrolled in program '" + sessionDto.getProgramId() + "'.");
            }

            Therapist therapist = session.get(Therapist.class, sessionDto.getTherapistId());
            Patient patient     = session.get(Patient.class,   sessionDto.getPatientId());
            Program program     = session.get(Program.class,   sessionDto.getProgramId());

            if (patient == null) throw new SchedulingException("Patient not found: " + sessionDto.getPatientId());
            if (program == null) throw new SchedulingException("Program not found: " + sessionDto.getProgramId());
            if (therapist == null) throw new SchedulingException("Therapist not found: " + sessionDto.getTherapistId());

            // Payment validation
            if (paymentDto.getFullAmount() <= 0) {
                throw new PaymentException("Payment amount must be greater than zero.");
            }
            double paid = paymentDto.getFullAmount() - paymentDto.getRemainingAmount();
            if (paid < 0) {
                throw new PaymentException("Amount paid cannot exceed the program fee.");
            }

            TherapySession ts = new TherapySession(sessionDto.getSessionDate(), patient, program, therapist);
            Payment payment   = new Payment(paymentDto.getPaymentDetails(),
                    paymentDto.getFullAmount(), paymentDto.getRemainingAmount(), ts);

            session.save(ts);
            session.save(payment);
            tx.commit();
            return true;
        } catch (SchedulingException | PaymentException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new SchedulingException("Failed to book session: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public boolean updateSession(TherapySessionDto sessionDto) throws SQLException, ClassNotFoundException {
        Session session = FactoryConfiguration.getInstance().getSession();
        try {
            Patient patient   = session.get(Patient.class,   sessionDto.getPatientId());
            Program program   = session.get(Program.class,   sessionDto.getProgramId());
            Therapist therapist = session.get(Therapist.class, sessionDto.getTherapistId());
            return sessionDAO.update(new TherapySession(sessionDto.getSessionDate(), patient, program, therapist));
        } finally {
            session.close();
        }
    }

    @Override
    public boolean deleteSession(String patientId) throws SQLException, ClassNotFoundException {
        return sessionDAO.delete(patientId);
    }

    @Override
    public String search(String programId) {
        return sessionDAO.search(programId);
    }

    @Override
    public List<String> getProgramIds(String patientId) {
        return sessionDAO.getProgramIds(patientId);
    }

    @Override
    public Long searchSessionId(String patientId, String programId) {
        return sessionDAO.searchSessionId(patientId, programId);
    }

    @Override
    public List<TherapyProgramHistoryDto> getPatientTherapyHistory(String patientId) {
        List<Object[]> results = sessionDAO.getPatientTherapyHistory(patientId);
        List<TherapyProgramHistoryDto> list = new ArrayList<>();
        for (Object[] row : results) {
            TherapyProgramHistoryDto dto = new TherapyProgramHistoryDto();
            dto.setProgramId((String) row[0]);
            dto.setProgramName((String) row[1]);
            dto.setTherapistName((String) row[2]);
            dto.setFee((String) row[3]);
            dto.setRemainingAmount(row[4] != null ? (Double) row[4] : 0.0);
            dto.setPaymentDate((String) row[5]);
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<String> getPatientIdsFromTherapySessions() {
        return sessionDAO.getPatientIdsFromTherapySessions();
    }

}
