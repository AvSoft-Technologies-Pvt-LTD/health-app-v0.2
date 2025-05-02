package com.avswasthya.healthapp.doctor.model;

import com.avswasthya.healthapp.appointment.model.Appointment;
import com.avswasthya.healthapp.auth.model.User;
import com.avswasthya.healthapp.hospital.model.Hospital;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String license;

    @Column(nullable = false)
    private String specialty;

    @Column(nullable = false)
    private String degree;

    @Column(name="isFreelancer")
    private boolean isFreelancer;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "doc_id", referencedColumnName = "user_id")
    private User user;
}
