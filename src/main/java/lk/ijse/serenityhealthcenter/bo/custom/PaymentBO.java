package lk.ijse.serenityhealthcenter.bo.custom;

import lk.ijse.serenityhealthcenter.dto.PaymentDTO;

import java.util.List;

public interface PaymentBO {
    Long savePayment(PaymentDTO paymentDTO);
    void updatePayment(PaymentDTO paymentDTO);
    void deletePayment(Long id);
    PaymentDTO getPayment(Long id);
    List<PaymentDTO> getAllPayments();
    List<PaymentDTO> getPaymentsByPatient(Long patientId);
}
