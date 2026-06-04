package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.dao.CrudDAO;
import lk.ijse.serenityhealthcenter.dto.TherapistDto;
import lk.ijse.serenityhealthcenter.entity.Therapist;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

public interface TherapistDAO extends CrudDAO<Therapist> {
    Serializable saveTherapist(Therapist therapist, Session session);
    List<String> getAvailableTherapists();
    void updateStatus(TherapistDto therapistDto);
    Serializable updateTherapist(Therapist therapist, Session session);
}
