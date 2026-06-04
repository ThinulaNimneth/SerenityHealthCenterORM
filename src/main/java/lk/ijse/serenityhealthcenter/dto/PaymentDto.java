package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDto {
    private long paymentId;
    private String paymentDetails;
    private double fullAmount;
    private double remainingAmount;

    public PaymentDto(String paymentDetails, double fullAmount, double remainingAmount) {
        this.paymentDetails = paymentDetails;
        this.fullAmount = fullAmount;
        this.remainingAmount = remainingAmount;
    }
}