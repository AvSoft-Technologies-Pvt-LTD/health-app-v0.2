package com.avswasthya.healthapp.patient.model;

import com.avswasthya.healthapp.appointment.model.Appointment;
import com.avswasthya.healthapp.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(length = 40)
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String healthCardId;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false, unique = true)
    private String aadharNumber;

    @Column(name="permanentAddress",length = 100)
    private String permanentAddress;

    @Column(name="temporaryAddress",length = 100)
    private String temporaryAddress;

    @Column(name="allergy")
    private String allergy;

    @Column(name= "height")
    private float height;

    @Column(name="weight")
    private float weight;

    @Column(name = "doesSmoke")
    private Boolean doesSmoke;

    @Column(name = "doesDrinkAlcohol")
    private Boolean doesDrinkAlcohol;

    @Column(name="blood_group",length = 5)
    private String bloodGroup;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_img")
    private byte[] photo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "pat_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FamilyHistory> familyHistories;


}
