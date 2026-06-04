package lk.ijse.serenityhealthcenter.bo.custom.impl;

import lk.ijse.serenityhealthcenter.bo.custom.PaymentBO;
import lk.ijse.serenityhealthcenter.dao.custom.PaymentDAO;
import lk.ijse.serenityhealthcenter.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.serenityhealthcenter.dto.PaymentDto;
import lk.ijse.serenityhealthcenter.entity.Payment;
import lk.ijse.serenityhealthcenter.entity.Patient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {
    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);

    @Override
    public PaymentDto searchPayment(String sessionId) {
        Payment p = paymentDAO.search(sessionId);
        if (p == null) return null;
        return new PaymentDto(p.getPaymentId(), p.getPaymentDetails(), p.getFullAmount(), p.getRemainingAmount());
    }

    @Override
    public boolean pay(String paymentId, String payingAmount) {
        return paymentDAO.pay(paymentId, payingAmount);
    }


}