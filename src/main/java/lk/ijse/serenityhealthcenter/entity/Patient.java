package lk.ijse.serenityhealthcenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "patient")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Patient {
    @Id
    private String id;
    private String name;
    private String email;
    private String address;
    private String tel;
    private String registerDate;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<TherapySession> therapySessions;

    public Patient(String id, String name, String email, String address, String tel, String registerDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.tel = tel;
        this.registerDate = registerDate;
    }
}