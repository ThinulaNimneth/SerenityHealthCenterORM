package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PaymentDTO {
    private Long paymentId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String transactionReference;
    private String description;
    private String status;
    private Long patientId;
    private String patientName;
}
