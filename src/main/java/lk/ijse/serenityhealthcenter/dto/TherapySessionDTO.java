package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapySessionDTO {
    private Long sessionId;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private Integer durationMinutes;
    private String status;
    private String notes;
    private Long patientId;
    private String patientName;
    private Long therapistId;
    private String therapistName;
    private String programId;
    private String programName;
}
