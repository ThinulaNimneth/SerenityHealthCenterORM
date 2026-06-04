package lk.ijse.serenityhealthcenter.bo.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.serenityhealthcenter.bo.custom.ProgramBO;
import lk.ijse.serenityhealthcenter.dao.custom.ProgramDAO;
import lk.ijse.serenityhealthcenter.dto.ProgramDto;
import lk.ijse.serenityhealthcenter.entity.Program;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramBOImpl implements ProgramBO {
    ProgramDAO programDAO = (ProgramDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PROGRAM);

    @Override
    public ObservableList<ProgramDto> getAllPrograms() throws SQLException, ClassNotFoundException {
        List<Program> programs = programDAO.getAll();
        List<ProgramDto> dtos = new ArrayList<>();
        for (Program p : programs) {
            dtos.add(new ProgramDto(p.getProgramId(), p.getName(), p.getDuration(), p.getFee()));
        }
        return FXCollections.observableArrayList(dtos);
    }

    @Override
    public boolean addProgram(ProgramDto dto) throws SQLException, ClassNotFoundException {
        return programDAO.save(new Program(dto.getProgramId(), dto.getName(), dto.getDuration(), dto.getFee()));
    }

    @Override
    public boolean updateProgram(ProgramDto dto) throws SQLException, ClassNotFoundException {
        return programDAO.update(new Program(dto.getProgramId(), dto.getName(), dto.getDuration(), dto.getFee()));
    }

    @Override
    public boolean deleteProgram(String programId) throws SQLException, ClassNotFoundException {
        return programDAO.delete(programId);
    }

    @Override
    public ProgramDto searchProgram(String programId) {
        Program p = programDAO.search(programId);
        if (p == null) return null;
        return new ProgramDto(p.getProgramId(), p.getName(), p.getDuration(), p.getFee());
    }
}
