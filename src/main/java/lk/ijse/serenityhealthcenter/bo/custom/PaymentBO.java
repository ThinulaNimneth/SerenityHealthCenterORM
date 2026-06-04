package lk.ijse.serenityhealthcenter.bo.custom;

import lk.ijse.serenityhealthcenter.bo.SuperBO;
import lk.ijse.serenityhealthcenter.dto.PaymentDto;

import java.util.List;

public interface PaymentBO extends SuperBO {
    PaymentDto searchPayment(String sessionId);
    boolean pay(String paymentId, String payingAmount);
}