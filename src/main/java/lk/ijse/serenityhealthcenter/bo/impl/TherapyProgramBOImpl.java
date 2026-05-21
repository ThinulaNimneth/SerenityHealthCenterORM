package lk.ijse.serenityhealthcenter.bo.impl;

import lk.ijse.serenityhealthcenter.bo.custom.TherapyProgramBO;
import lk.ijse.serenityhealthcenter.dao.custom.TherapyProgramDAO;
import lk.ijse.serenityhealthcenter.dao.impl.TherapyProgramDAOImpl;
import lk.ijse.serenityhealthcenter.dto.TherapyProgramDTO;
import lk.ijse.serenityhealthcenter.entity.TherapyProgram;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class TherapyProgramBOImpl implements TherapyProgramBO{
    private final TherapyProgramDAO programDAO = new TherapyProgramDAOImpl();

    @Override
    public String saveProgram(TherapyProgramDTO dto) {
        TherapyProgram program = new TherapyProgram();
        program.setProgramId(dto.getProgramId());
        program.setProgramName(dto.getProgramName());
        program.setDuration(dto.getDuration());
        program.setFee(dto.getFee());
        program.setDescription(dto.getDescription());
        program.setIsActive(true);
        return programDAO.save(program);
    }

    @Override
    public void updateProgram(TherapyProgramDTO dto) {
        Optional<TherapyProgram> existing = programDAO.findById(dto.getProgramId());
        if (existing.isPresent()) {
            TherapyProgram program = existing.get();
            program.setProgramName(dto.getProgramName());
            program.setDuration(dto.getDuration());
            program.setFee(dto.getFee());
            program.setDescription(dto.getDescription());
            program.setIsActive(dto.getIsActive());
            programDAO.update(program);
        }
    }

    @Override
    public void deleteProgram(String id) {
        programDAO.delete(id);
    }

    @Override
    public TherapyProgramDTO getProgram(String id) {
        Optional<TherapyProgram> program = programDAO.findById(id);
        return program.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<TherapyProgramDTO> getAllPrograms() {
        return programDAO.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TherapyProgramDTO> getActivePrograms() {
        return programDAO.findActive().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TherapyProgramDTO convertToDTO(TherapyProgram program) {
        TherapyProgramDTO dto = new TherapyProgramDTO();
        dto.setProgramId(program.getProgramId());
        dto.setProgramName(program.getProgramName());
        dto.setDuration(program.getDuration());
        dto.setFee(program.getFee());
        dto.setDescription(program.getDescription());
        dto.setIsActive(program.getIsActive());
        return dto;
    }
}
