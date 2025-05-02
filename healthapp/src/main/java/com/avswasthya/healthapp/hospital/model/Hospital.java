package com.avswasthya.healthapp.hospital.model;

import com.avswasthya.healthapp.doctor.model.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "hospital")
@Getter
@Setter
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="hospitalName",unique = true, nullable = false)
    private String hospitalName;

    @Column(name="address",unique = true, nullable = false)
    private String hospitalAddress;

    @Column(name="Type",nullable = false)
    private String hospitalType;

    @Column(name="City",nullable = false)
    private String hospitalCity;

    @Column(name="CEO",nullable = false)
    private String hospitalCEO;


    @Column(name="phone",unique = true, nullable = false)
    private String hospitalPhone;

    @Column(name="GSTNumber",unique = true, nullable = false)
    private String hospitalGSTNumber;

    @Lob
    @Column(name="nabhCertification",nullable = false)
    private byte[] nabhCertificate;

    @Lob
    @Column(name="image")
    private byte[] hospitalImage;

    @Column(name="scan_labs")
    private String hospitalScan;

    @Column(name="hospital_Pharmacy")
    private String hospitalPharmacy;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctor> doctors;
}
