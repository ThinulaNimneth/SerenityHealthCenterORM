package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PatientDto {
    private String id;
    private String name;
    private String email;
    private String address;
    private String tel;
    private String registerDate;
}
