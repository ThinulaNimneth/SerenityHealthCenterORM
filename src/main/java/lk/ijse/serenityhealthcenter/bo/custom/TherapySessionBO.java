package lk.ijse.serenityhealthcenter.bo.custom;

import lk.ijse.serenityhealthcenter.dto.TherapySessionDTO;

import java.time.LocalDate;
import java.util.List;

public interface TherapySessionBO {
    Long saveSession(TherapySessionDTO sessionDTO);
    void updateSession(TherapySessionDTO sessionDTO);
    void deleteSession(Long id);
    TherapySessionDTO getSession(Long id);
    List<TherapySessionDTO> getAllSessions();
    List<TherapySessionDTO> getSessionsByPatient(Long patientId);
    List<TherapySessionDTO> getSessionsByTherapist(Long therapistId);
    List<TherapySessionDTO> getSessionsByDate(LocalDate date);
}
