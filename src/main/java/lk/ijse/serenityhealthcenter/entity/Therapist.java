package lk.ijse.serenityhealthcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "therapists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "sessions")

public class Therapist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "therapist_id")
    private Long therapistId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(nullable = false, length = 100)
    private String specialization;

    @Column(length = 20)
    private String qualification;

    @Column(name = "license_number", length = 50)
    private String licenseNumber;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    // One-to-Many: One therapist can conduct many therapy sessions
    @OneToMany(mappedBy = "therapist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TherapySession> sessions = new ArrayList<>();
}
