package lk.ijse.serenityhealthcenter.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PatientDTO {
    private Long patientId;
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String medicalHistory;
    private LocalDate registrationDate;
    private String emergencyContact;
    private List<String> enrolledProgramIds; // List of program IDs
}
