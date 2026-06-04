package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TherapyProgramHistoryDto {
    private String programId;
    private String programName;
    private String therapistName;
    private String fee;
    private double remainingAmount;
    private String paymentDate;
}
