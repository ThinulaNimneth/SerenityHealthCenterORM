package lk.ijse.serenityhealthcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "program")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Program {
    @Id
    private String programId;
    private String name;
    private String duration;
    private String fee;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<TherapistProgram> therapistPrograms;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<TherapySession> therapySessions;

    public Program(String programId, String name, String duration, String fee) {
        this.programId = programId;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
    }
}
