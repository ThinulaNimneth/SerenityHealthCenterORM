package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.entity.TherapyProgram;

import java.util.List;
import java.util.Optional;

public interface TherapyProgramDAO {
    String save(TherapyProgram program);
    void update(TherapyProgram program);
    void delete(String id);
    Optional<TherapyProgram> findById(String id);
    List<TherapyProgram> findAll();
    List<TherapyProgram> findActive();
    long countAllPrograms();
}
