package lk.ijse.serenityhealthcenter.bo.impl;

import lk.ijse.serenityhealthcenter.bo.custom.TherapySessionBO;
import lk.ijse.serenityhealthcenter.dao.custom.TherapySessionDAO;
import lk.ijse.serenityhealthcenter.dao.impl.TherapySessionDAOImpl;
import lk.ijse.serenityhealthcenter.dto.TherapySessionDTO;
import lk.ijse.serenityhealthcenter.entity.TherapySession;
import lk.ijse.serenityhealthcenter.entity.Patient;
import lk.ijse.serenityhealthcenter.entity.Therapist;
import lk.ijse.serenityhealthcenter.entity.TherapyProgram;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TherapySessionBOImpl implements TherapySessionBO {

    private final TherapySessionDAO sessionDAO = new TherapySessionDAOImpl();

    @Override
    public Long saveSession(TherapySessionDTO dto) {
        TherapySession session = new TherapySession();
        session.setSessionDate(dto.getSessionDate());
        session.setSessionTime(dto.getSessionTime());
        session.setDurationMinutes(dto.getDurationMinutes());
        session.setStatus(TherapySession.SessionStatus.valueOf(dto.getStatus()));
        session.setNotes(dto.getNotes());

        Patient patient = new Patient();
        patient.setPatientId(dto.getPatientId());
        session.setPatient(patient);

        Therapist therapist = new Therapist();
        therapist.setTherapistId(dto.getTherapistId());
        session.setTherapist(therapist);

        TherapyProgram program = new TherapyProgram();
        program.setProgramId(dto.getProgramId());
        session.setTherapyProgram(program);

        return sessionDAO.save(session);
    }

    @Override
    public void updateSession(TherapySessionDTO dto) {
        Optional<TherapySession> existing = sessionDAO.findById(dto.getSessionId());
        if (existing.isPresent()) {
            TherapySession session = existing.get();
            session.setSessionDate(dto.getSessionDate());
            session.setSessionTime(dto.getSessionTime());
            session.setDurationMinutes(dto.getDurationMinutes());
            session.setStatus(TherapySession.SessionStatus.valueOf(dto.getStatus()));
            session.setNotes(dto.getNotes());
            sessionDAO.update(session);
        }
    }

    @Override
    public void deleteSession(Long id) {
        sessionDAO.delete(id);
    }

    @Override
    public TherapySessionDTO getSession(Long id) {
        Optional<TherapySession> session = sessionDAO.findById(id);
        return session.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<TherapySessionDTO> getAllSessions() {
        return sessionDAO.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TherapySessionDTO> getSessionsByPatient(Long patientId) {
        return sessionDAO.findByPatient(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TherapySessionDTO> getSessionsByTherapist(Long therapistId) {
        return sessionDAO.findByTherapist(therapistId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TherapySessionDTO> getSessionsByDate(LocalDate date) {
        return sessionDAO.findByDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TherapySessionDTO convertToDTO(TherapySession session) {
        TherapySessionDTO dto = new TherapySessionDTO();
        dto.setSessionId(session.getSessionId());
        dto.setSessionDate(session.getSessionDate());
        dto.setSessionTime(session.getSessionTime());
        dto.setDurationMinutes(session.getDurationMinutes());
        dto.setStatus(session.getStatus().name());
        dto.setNotes(session.getNotes());
        dto.setPatientId(session.getPatient().getPatientId());
        dto.setPatientName(session.getPatient().getName());
        dto.setTherapistId(session.getTherapist().getTherapistId());
        dto.setTherapistName(session.getTherapist().getName());
        dto.setProgramId(session.getTherapyProgram().getProgramId());
        dto.setProgramName(session.getTherapyProgram().getProgramName());
        return dto;
    }
}