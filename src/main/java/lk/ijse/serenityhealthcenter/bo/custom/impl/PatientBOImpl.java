package lk.ijse.serenityhealthcenter.bo.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.serenityhealthcenter.bo.custom.PatientBO;
import lk.ijse.serenityhealthcenter.dao.custom.PatientDAO;
import lk.ijse.serenityhealthcenter.dao.custom.impl.PatientDAOImpl;
import lk.ijse.serenityhealthcenter.dto.PatientDto;
import lk.ijse.serenityhealthcenter.entity.Patient;
import lk.ijse.serenityhealthcenter.entity.Program;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientBOImpl implements PatientBO {
    PatientDAO patientDAO = (PatientDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PATIENT);

    @Override
    public ObservableList<PatientDto> getAllPatients() throws SQLException, ClassNotFoundException {
        List<Patient> patients = patientDAO.getAll();
        List<PatientDto> dtos = new ArrayList<>();
        for (Patient p : patients) {
            dtos.add(new PatientDto(p.getId(), p.getName(), p.getEmail(),
                    p.getAddress(), p.getTel(), p.getRegisterDate()));
        }
        return FXCollections.observableArrayList(dtos);
    }

    @Override
    public boolean addPatient(PatientDto dto) throws SQLException, ClassNotFoundException {
        // RegistrationException thrown by DAO if duplicate — propagates up to controller
        return patientDAO.save(new Patient(dto.getId(), dto.getName(), dto.getEmail(),
                dto.getAddress(), dto.getTel(), dto.getRegisterDate()));
    }

    @Override
    public boolean updatePatient(PatientDto dto) throws SQLException, ClassNotFoundException {
        return patientDAO.update(new Patient(dto.getId(), dto.getName(), dto.getEmail(),
                dto.getAddress(), dto.getTel(), dto.getRegisterDate()));
    }

    @Override
    public boolean deletePatient(String id) throws SQLException, ClassNotFoundException {
        return patientDAO.delete(id);
    }

    @Override
    public PatientDto searchPatient(String id) {
        Patient p = patientDAO.search(id);
        if (p == null) return null;
        return new PatientDto(p.getId(), p.getName(), p.getEmail(),
                p.getAddress(), p.getTel(), p.getRegisterDate());
    }

    // returns patients enrolled in every available program
    @Override
    public List<PatientDto> getPatientsEnrolledInAllPrograms() {
        List<Patient> patients = patientDAO.getPatientsEnrolledInAllPrograms();
        List<PatientDto> dtos = new ArrayList<>();
        for (Patient p : patients) {
            dtos.add(new PatientDto(p.getId(), p.getName(), p.getEmail(),
                    p.getAddress(), p.getTel(), p.getRegisterDate()));
        }
        return dtos;
    }

    // returns readable strings
    @Override
    public List<String> getPatientsWithTherapyPrograms() {
        List<Object[]> rows = patientDAO.getPatientsWithTherapyPrograms();
        List<String> result = new ArrayList<>();
        for (Object[] row : rows) {
            Patient p  = (Patient) row[0];
            Program pr = (Program) row[1];
            result.add(p.getName() + " [" + p.getId() + "]  →  " + pr.getName() + " (" + pr.getProgramId() + ")");
        }
        return result;
    }

}