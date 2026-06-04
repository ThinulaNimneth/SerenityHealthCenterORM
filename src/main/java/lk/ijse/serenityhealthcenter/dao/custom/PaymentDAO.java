package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.dao.CrudDAO;
import lk.ijse.serenityhealthcenter.entity.Payment;

import java.util.List;


public interface PaymentDAO extends CrudDAO<Payment> {
    Payment search(String sessionId);
    boolean pay(String paymentId, String payingAmount);
    List<Payment> getAllByPatient(String patientId);

}