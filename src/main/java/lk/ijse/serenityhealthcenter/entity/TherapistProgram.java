package lk.ijse.serenityhealthcenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "therapist_program")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TherapistProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "therapist_id")
    private Therapist therapist;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    public TherapistProgram(Therapist therapist, Program program) {
        this.therapist = therapist;
        this.program = program;
    }
}
