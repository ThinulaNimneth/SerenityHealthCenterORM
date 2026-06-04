package lk.ijse.serenityhealthcenter.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TherapistDto {
    private String therapistId;
    private String name;
    private String specialization;
    private String contactNo;
    private String status;
}
