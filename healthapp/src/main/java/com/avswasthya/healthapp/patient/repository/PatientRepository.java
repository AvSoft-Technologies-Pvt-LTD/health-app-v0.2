package com.avswasthya.healthapp.patient.repository;

import com.avswasthya.healthapp.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByAadharNumber(String aadharNumber);

    boolean existsByHealthCardId(String healthCardId);
}
