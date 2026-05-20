package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientDAO {

    Long save(Patient patient);
    void update(Patient patient);
    void delete(Long id);
    Optional<Patient> findById(Long id);
    List<Patient> findAll();
    Optional<Patient> findByEmail(String email);
    List<Patient> searchByName(String name);
    //all program patient enroll get
    List<Patient> findPatientsEnrolledInAllPrograms();
    // patient with enroll program
    Optional<Patient> findPatientWithPrograms(Long patientId);
}
