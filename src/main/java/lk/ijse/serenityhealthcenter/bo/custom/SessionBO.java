package lk.ijse.serenityhealthcenter.bo.custom;

import lk.ijse.serenityhealthcenter.bo.SuperBO;
import lk.ijse.serenityhealthcenter.dto.PaymentDto;
import lk.ijse.serenityhealthcenter.dto.TherapyProgramHistoryDto;
import lk.ijse.serenityhealthcenter.dto.TherapySessionDto;

import java.sql.SQLException;
import java.util.List;

public interface SessionBO extends SuperBO {
    boolean addSession(TherapySessionDto sessionDto, PaymentDto paymentDto) throws SQLException, ClassNotFoundException;
    boolean updateSession(TherapySessionDto sessionDto) throws SQLException, ClassNotFoundException;
    boolean deleteSession(String patientId) throws SQLException, ClassNotFoundException;
    String search(String programId);
    List<String> getProgramIds(String patientId);
    Long searchSessionId(String patientId, String programId);
    List<TherapyProgramHistoryDto> getPatientTherapyHistory(String patientId);
    List<String> getPatientIdsFromTherapySessions();
}
