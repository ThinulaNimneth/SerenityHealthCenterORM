package lk.ijse.serenityhealthcenter.bo.custom;

import javafx.collections.ObservableList;
import lk.ijse.serenityhealthcenter.bo.SuperBO;
import lk.ijse.serenityhealthcenter.dto.PatientDto;

import java.sql.SQLException;
import java.util.List;

public interface PatientBO  extends SuperBO {
    ObservableList<PatientDto> getAllPatients() throws SQLException, ClassNotFoundException;
    boolean addPatient(PatientDto patientDto) throws SQLException, ClassNotFoundException;
    boolean updatePatient(PatientDto patientDto) throws SQLException, ClassNotFoundException;
    boolean deletePatient(String id) throws SQLException, ClassNotFoundException;
    PatientDto searchPatient(String id);

    // patient with enrolled programs
    List<PatientDto> getPatientsEnrolledInAllPrograms();

    // patient with therapy programs
    List<String> getPatientsWithTherapyPrograms();
}