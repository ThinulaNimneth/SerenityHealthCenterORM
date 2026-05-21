package lk.ijse.serenityhealthcenter.bo.custom;

import lk.ijse.serenityhealthcenter.dto.TherapistDTO;

import java.util.List;

public interface TherapistBO {
    Long saveTherapist(TherapistDTO therapistDTO);
    void updateTherapist(TherapistDTO therapistDTO);
    void deleteTherapist(Long id);
    TherapistDTO getTherapist(Long id);
    List<TherapistDTO> getAllTherapists();
    List<TherapistDTO> getAvailableTherapists();
}
