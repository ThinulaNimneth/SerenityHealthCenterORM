package lk.ijse.serenityhealthcenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;
    private String paymentDetails;
    private double fullAmount;
    private double remainingAmount;

    @OneToOne
    @JoinColumn(name = "session_id")
    private TherapySession therapySession;

    public Payment(String paymentDetails, double fullAmount, double remainingAmount, TherapySession therapySession) {
        this.paymentDetails = paymentDetails;
        this.fullAmount = fullAmount;
        this.remainingAmount = remainingAmount;
        this.therapySession = therapySession;
    }
}
