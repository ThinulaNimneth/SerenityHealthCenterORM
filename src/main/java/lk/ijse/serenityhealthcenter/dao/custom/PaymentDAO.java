package lk.ijse.serenityhealthcenter.dao.custom;

import lk.ijse.serenityhealthcenter.entity.Payment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentDAO {
    Long save(Payment payment);
    void update(Payment payment);
    void delete(Long id);
    Optional<Payment> findById(Long id);
    List<Payment> findAll();
    List<Payment> findByPatient(Long patientId);
    List<Payment> findByDateRange(LocalDate start, LocalDate end);
    List<Payment> findByStatus(Payment.PaymentStatus status);
}
