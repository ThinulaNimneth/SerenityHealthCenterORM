package lk.ijse.serenityhealthcenter.bo.custom;

import javafx.collections.ObservableList;
import lk.ijse.serenityhealthcenter.bo.SuperBO;
import lk.ijse.serenityhealthcenter.dto.ProgramDto;

import java.sql.SQLException;

public interface ProgramBO extends SuperBO {
    ObservableList<ProgramDto> getAllPrograms() throws SQLException, ClassNotFoundException;
    boolean addProgram(ProgramDto programDto) throws SQLException, ClassNotFoundException;
    boolean updateProgram(ProgramDto programDto) throws SQLException, ClassNotFoundException;
    boolean deleteProgram(String programId) throws SQLException, ClassNotFoundException;
    ProgramDto searchProgram(String programId);
}
