package lk.ijse.serenityhealthcenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "therapy_session")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TherapySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sessionId;
    private String sessionDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;

    public TherapySession(String sessionDate, Patient patient, Program program, Therapist therapist) {
        this.sessionDate = sessionDate;
        this.patient = patient;
        this.program = program;
        this.therapist = therapist;
    }
}
