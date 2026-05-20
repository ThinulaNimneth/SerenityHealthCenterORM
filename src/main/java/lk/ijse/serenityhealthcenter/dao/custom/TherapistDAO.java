package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.entity.Therapist;

import java.util.List;
import java.util.Optional;

public interface TherapistDAO {
    Long save(Therapist therapist);
    void update(Therapist therapist);
    void delete(Long id);
    Optional<Therapist> findById(Long id);
    List<Therapist> findAll();
    List<Therapist> findAvailable();
    List<Therapist> findBySpecialization(String specialization);
    Optional<Therapist> findByEmail(String email);
}
