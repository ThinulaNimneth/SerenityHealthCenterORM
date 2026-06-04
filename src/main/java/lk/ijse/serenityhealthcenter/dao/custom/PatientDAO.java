package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.dao.CrudDAO;
import lk.ijse.serenityhealthcenter.entity.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientDAO extends CrudDAO<Patient> {
    Patient search(String id);

    //patient enrolled in every program
    List<Patient> getPatientsEnrolledInAllPrograms();

    // fetch patients with their enrolled therapy programs
    List<Object[]> getPatientsWithTherapyPrograms();
}