package lk.ijse.serenityhealthcenter.bo.custom;

import lk.ijse.serenityhealthcenter.dto.PatientDTO;
import lk.ijse.serenityhealthcenter.util.CustomExceptions;

import java.util.List;

public interface PatientBO {
    Long savePatient(PatientDTO patientDTO) throws CustomExceptions.RegistrationException, CustomExceptions.ValidationException;
    void updatePatient(PatientDTO patientDTO) throws CustomExceptions.RegistrationException;
    void deletePatient(Long id);
    PatientDTO getPatient(Long id);
    List<PatientDTO> getAllPatients();
    List<PatientDTO> searchPatients(String name);
    void enrollInProgram(Long patientId, String programId) throws CustomExceptions.RegistrationException;
    List<PatientDTO> getPatientsEnrolledInAllPrograms();
    PatientDTO getPatientWithPrograms(Long patientId);
}