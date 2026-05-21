package lk.ijse.serenityhealthcenter.bo.custom;

import lk.ijse.serenityhealthcenter.dto.TherapyProgramDTO;

import java.util.List;

public interface TherapyProgramBO {
    String saveProgram(TherapyProgramDTO programDTO);
    void updateProgram(TherapyProgramDTO programDTO);
    void deleteProgram(String id);
    TherapyProgramDTO getProgram(String id);
    List<TherapyProgramDTO> getAllPrograms();
    List<TherapyProgramDTO> getActivePrograms();
}
