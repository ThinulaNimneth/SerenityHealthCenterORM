package lk.ijse.serenityhealthcenter.bo.custom;

import javafx.collections.ObservableList;
import lk.ijse.serenityhealthcenter.bo.SuperBO;
import lk.ijse.serenityhealthcenter.dto.TherapistDto;

import java.sql.SQLException;
import java.util.List;

public interface TherapistBO extends SuperBO {
    ObservableList<TherapistDto> getAllTherapists() throws SQLException, ClassNotFoundException;
    void addTherapist(TherapistDto therapistDto, String programId) throws SQLException, ClassNotFoundException;
    boolean deleteTherapist(String id) throws SQLException, ClassNotFoundException;
    boolean updateTherapist(TherapistDto therapistDto, String programId) throws SQLException, ClassNotFoundException;
    List<String> getAvailableTherapistIds();
}
