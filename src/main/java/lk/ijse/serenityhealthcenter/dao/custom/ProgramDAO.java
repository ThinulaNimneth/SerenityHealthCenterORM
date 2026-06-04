package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.dao.CrudDAO;
import lk.ijse.serenityhealthcenter.entity.Program;

public interface ProgramDAO extends CrudDAO<Program> {
    Program get(String programId);
    Program search(String programId);
}
