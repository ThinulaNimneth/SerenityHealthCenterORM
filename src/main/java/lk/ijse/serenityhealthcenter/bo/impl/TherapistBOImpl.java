package lk.ijse.serenityhealthcenter.bo.impl;

import lk.ijse.serenityhealthcenter.bo.custom.TherapistBO;


import lk.ijse.serenityhealthcenter.dao.custom.TherapistDAO;
import lk.ijse.serenityhealthcenter.dao.impl.TherapistDAOImpl;
import lk.ijse.serenityhealthcenter.dto.TherapistDTO;
import lk.ijse.serenityhealthcenter.entity.Therapist;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TherapistBOImpl  implements TherapistBO {

    private final TherapistDAO therapistDAO = new TherapistDAOImpl();

    @Override
    public Long saveTherapist(TherapistDTO dto) {
        Therapist therapist = new Therapist();
        therapist.setName(dto.getName());
        therapist.setEmail(dto.getEmail());
        therapist.setPhone(dto.getPhone());
        therapist.setSpecialization(dto.getSpecialization());
        therapist.setQualification(dto.getQualification());
        therapist.setLicenseNumber(dto.getLicenseNumber());
        therapist.setYearsOfExperience(dto.getYearsOfExperience());
        therapist.setIsAvailable(true);
        return therapistDAO.save(therapist);
    }

    @Override
    public void updateTherapist(TherapistDTO dto) {
        Optional<Therapist> existing = therapistDAO.findById(dto.getTherapistId());
        if (existing.isPresent()) {
            Therapist therapist = existing.get();
            therapist.setName(dto.getName());
            therapist.setEmail(dto.getEmail());
            therapist.setPhone(dto.getPhone());
            therapist.setSpecialization(dto.getSpecialization());
            therapist.setQualification(dto.getQualification());
            therapist.setLicenseNumber(dto.getLicenseNumber());
            therapist.setYearsOfExperience(dto.getYearsOfExperience());
            therapist.setIsAvailable(dto.getIsAvailable());
            therapistDAO.update(therapist);
        }
    }

    @Override
    public void deleteTherapist(Long id) {
        therapistDAO.delete(id);
    }

    @Override
    public TherapistDTO getTherapist(Long id) {
        Optional<Therapist> therapist = therapistDAO.findById(id);
        return therapist.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<TherapistDTO> getAllTherapists() {
        return therapistDAO.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TherapistDTO> getAvailableTherapists() {
        return therapistDAO.findAvailable().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TherapistDTO convertToDTO(Therapist therapist) {
        TherapistDTO dto = new TherapistDTO();
        dto.setTherapistId(therapist.getTherapistId());
        dto.setName(therapist.getName());
        dto.setEmail(therapist.getEmail());
        dto.setPhone(therapist.getPhone());
        dto.setSpecialization(therapist.getSpecialization());
        dto.setQualification(therapist.getQualification());
        dto.setLicenseNumber(therapist.getLicenseNumber());
        dto.setYearsOfExperience(therapist.getYearsOfExperience());
        dto.setIsAvailable(therapist.getIsAvailable());
        return dto;
    }

}
