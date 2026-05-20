package lk.ijse.serenityhealthcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "therapy_programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"patients", "sessions"})

public class TherapyProgram {

    @Id
    @Column(name = "program_id", length = 10)
    private String programId;

    @Column(name = "program_name", nullable = false, length = 100)
    private String programName;

    @Column(nullable = false, length = 50)
    private String duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @Column(length = 1000)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToMany(mappedBy = "enrolledPrograms", fetch = FetchType.LAZY)
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "therapyProgram", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TherapySession> sessions = new ArrayList<>();
}
