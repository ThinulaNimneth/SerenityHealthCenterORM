package lk.ijse.serenityhealthcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "therapist")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Therapist {
    @Id
    private String therapistId;
    private String name;
    private String specialization;
    private String contactNo;
    private String status;

    @OneToMany(mappedBy = "therapist")
    private List<TherapistProgram> therapistPrograms;

    @OneToMany(mappedBy = "therapist")
    private List<TherapySession> therapySessions;

    public Therapist(String therapistId, String name, String specialization, String contactNo, String status) {
        this.therapistId = therapistId;
        this.name = name;
        this.specialization = specialization;
        this.contactNo = contactNo;
        this.status = status;
    }
}
