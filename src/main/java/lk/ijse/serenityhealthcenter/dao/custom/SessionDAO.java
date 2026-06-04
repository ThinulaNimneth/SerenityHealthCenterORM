package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.dao.CrudDAO;
import lk.ijse.serenityhealthcenter.entity.TherapySession;
import java.util.List;

public interface SessionDAO extends CrudDAO<Session> {
    String search(String programId);
    List<String> getProgramIds(String patientId);
    Long searchSessionId(String patientId, String programId);
    List<Object[]> getPatientTherapyHistory(String patientId);
    List<String> getPatientIdsFromTherapySessions();
}
