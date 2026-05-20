package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TherapistDTO {
    private Long therapistId;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private String qualification;
    private String licenseNumber;
    private Integer yearsOfExperience;
    private Boolean isAvailable;
}
