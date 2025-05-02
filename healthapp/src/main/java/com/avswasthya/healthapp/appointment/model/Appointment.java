package com.avswasthya.healthapp.appointment.model;

import com.avswasthya.healthapp.doctor.model.Doctor;
import com.avswasthya.healthapp.enums.AppointmentStatus;
import com.avswasthya.healthapp.hospital.model.Hospital;
import com.avswasthya.healthapp.patient.model.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pat_id", nullable = false)
    private Patient patient;

    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column(name = "consultation_type", nullable = false)
    private String consultationType;  // Consider changing this to an enum if fixed values

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hospital_id")
    private Hospital hospital;
}
