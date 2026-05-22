package lk.ijse.serenityhealthcenter.bo.impl;

import lk.ijse.serenityhealthcenter.bo.custom.PatientBO;
import lk.ijse.serenityhealthcenter.dao.custom.PatientDAO;
import lk.ijse.serenityhealthcenter.dao.custom.TherapyProgramDAO;
import lk.ijse.serenityhealthcenter.dao.impl.PatientDAOImpl;
import lk.ijse.serenityhealthcenter.dao.impl.TherapyProgramDAOImpl;
import lk.ijse.serenityhealthcenter.dto.PatientDTO;
import lk.ijse.serenityhealthcenter.entity.Patient;
import lk.ijse.serenityhealthcenter.entity.TherapyProgram;
import lk.ijse.serenityhealthcenter.util.CustomExceptions;
import lk.ijse.serenityhealthcenter.util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientBOImpl implements PatientBO {
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final TherapyProgramDAO programDAO = new TherapyProgramDAOImpl();

    @Override
    public Long savePatient(PatientDTO dto) throws CustomExceptions.RegistrationException, CustomExceptions.ValidationException {
        // Validations
        if (!ValidationUtil.isNotEmpty(dto.getName())) {
            throw new CustomExceptions.MissingFieldException("Patient Name");
        }
        if (!ValidationUtil.isValidName(dto.getName())) {
            throw new CustomExceptions.ValidationException("Name", "Invalid name format");
        }
        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            throw new CustomExceptions.ValidationException("Email", "Invalid email format");
        }
        if (dto.getPhone() != null && !ValidationUtil.isValidPhone(dto.getPhone())) {
            throw new CustomExceptions.ValidationException("Phone", "Invalid phone number format");
        }

        // Check duplicate email
        if (patientDAO.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomExceptions.DuplicateEntryException("Email", dto.getEmail());
        }

        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setRegistrationDate(LocalDate.now());
        patient.setEmergencyContact(dto.getEmergencyContact());

        return patientDAO.save(patient);
    }

    @Override
    public void updatePatient(PatientDTO dto) throws CustomExceptions.RegistrationException {
        Optional<Patient> existingPatient = patientDAO.findById(dto.getPatientId());
        if (existingPatient.isEmpty()) {
            throw new CustomExceptions.RegistrationException("Patient not found");
        }

        Patient patient = existingPatient.get();
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setEmergencyContact(dto.getEmergencyContact());

        patientDAO.update(patient);
    }

    @Override
    public void deletePatient(Long id) {
        patientDAO.delete(id);
    }

    @Override
    public PatientDTO getPatient(Long id) {
        Optional<Patient> patient = patientDAO.findById(id);
        return patient.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        System.out.println("📊 BO getAllPatients() - START");
        List<Patient> patients = patientDAO.findAll();
        System.out.println("📊 BO - Retrieved " + patients.size() + " patients from DAO");

        if (patients.isEmpty()) {
            System.out.println("⚠️ WARNING: No patients found in database!");
            return new ArrayList<>();
        }

        List<PatientDTO> dtos = new ArrayList<>();
        for (Patient patient : patients) {
            PatientDTO dto = convertToDTO(patient);
            dtos.add(dto);
            System.out.println("   Converted: " + dto.getName() + " -> DTO");
        }

        System.out.println("📊 BO - Returning " + dtos.size() + " DTOs");
        return dtos;
    }

    @Override
    public List<PatientDTO> searchPatients(String name) {
        return patientDAO.searchByName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void enrollInProgram(Long patientId, String programId) throws CustomExceptions.RegistrationException {
        Optional<Patient> patientOpt = patientDAO.findById(patientId);
        Optional<TherapyProgram> programOpt = programDAO.findById(programId);

        if (patientOpt.isEmpty()) {
            throw new CustomExceptions.RegistrationException("Patient not found");
        }
        if (programOpt.isEmpty()) {
            throw new CustomExceptions.RegistrationException("Program not found");
        }

        Patient patient = patientOpt.get();
        TherapyProgram program = programOpt.get();

        if (!patient.getEnrolledPrograms().contains(program)) {
            patient.getEnrolledPrograms().add(program);
            patientDAO.update(patient);
        }
    }

    @Override
    public List<PatientDTO> getPatientsEnrolledInAllPrograms() {
        return patientDAO.findPatientsEnrolledInAllPrograms().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO getPatientWithPrograms(Long patientId) {
        Optional<Patient> patient = patientDAO.findPatientWithPrograms(patientId);
        return patient.map(p -> {
            PatientDTO dto = convertToDTO(p);
            dto.setEnrolledProgramIds(
                    p.getEnrolledPrograms().stream()
                            .map(TherapyProgram::getProgramId)
                            .collect(Collectors.toList())
            );
            return dto;
        }).orElse(null);
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setRegistrationDate(patient.getRegistrationDate());
        dto.setEmergencyContact(patient.getEmergencyContact());
        return dto;
    }
}