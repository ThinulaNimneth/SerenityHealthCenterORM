package lk.ijse.serenityhealthcenter.bo.impl;

import lk.ijse.serenityhealthcenter.bo.custom.PaymentBO;
import lk.ijse.serenityhealthcenter.dao.custom.PaymentDAO;
import lk.ijse.serenityhealthcenter.dao.impl.PaymentDAOImpl;
import lk.ijse.serenityhealthcenter.dto.PaymentDTO;
import lk.ijse.serenityhealthcenter.entity.Payment;
import lk.ijse.serenityhealthcenter.entity.Patient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();

    @Override
    public Long savePayment(PaymentDTO dto) {
        Payment payment = new Payment();
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(dto.getPaymentMethod()));
        payment.setTransactionReference(dto.getTransactionReference());
        payment.setDescription(dto.getDescription());
        payment.setStatus(Payment.PaymentStatus.valueOf(dto.getStatus()));

        Patient patient = new Patient();
        patient.setPatientId(dto.getPatientId());
        payment.setPatient(patient);

        return paymentDAO.save(payment);
    }

    @Override
    public void updatePayment(PaymentDTO dto) {
        Optional<Payment> existing = paymentDAO.findById(dto.getPaymentId());
        if (existing.isPresent()) {
            Payment payment = existing.get();
            payment.setAmount(dto.getAmount());
            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(dto.getPaymentMethod()));
            payment.setTransactionReference(dto.getTransactionReference());
            payment.setDescription(dto.getDescription());
            payment.setStatus(Payment.PaymentStatus.valueOf(dto.getStatus()));
            paymentDAO.update(payment);
        }
    }

    @Override
    public void deletePayment(Long id) {
        paymentDAO.delete(id);
    }

    @Override
    public PaymentDTO getPayment(Long id) {
        Optional<Payment> payment = paymentDAO.findById(id);
        return payment.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentDAO.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsByPatient(Long patientId) {
        return paymentDAO.findByPatient(patientId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod().name());
        dto.setTransactionReference(payment.getTransactionReference());
        dto.setDescription(payment.getDescription());
        dto.setStatus(payment.getStatus().name());
        dto.setPatientId(payment.getPatient().getPatientId());
        dto.setPatientName(payment.getPatient().getName());
        return dto;
    }
}