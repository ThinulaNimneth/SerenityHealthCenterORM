package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.entity.TherapySession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TherapySessionDAO {
    Long save(TherapySession session);
    void update(TherapySession session);
    void delete(Long id);
    Optional<TherapySession> findById(Long id);
    List<TherapySession> findAll();
    List<TherapySession> findByPatient(Long patientId);
    List<TherapySession> findByTherapist(Long therapistId);
    List<TherapySession> findByDate(LocalDate date);
    List<TherapySession> findByStatus(TherapySession.SessionStatus status);
}
