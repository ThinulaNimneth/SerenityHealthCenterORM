package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TherapySessionDto {
    private String patientId;
    private String programId;
    private String therapistId;
    private String sessionDate;
}

