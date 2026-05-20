package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TherapyProgramDTO {

    private String programId;
    private String programName;
    private String duration;
    private BigDecimal fee;
    private String description;
    private Boolean isActive;
}
