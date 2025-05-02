package com.avswasthya.healthapp.hospital.repository;

import com.avswasthya.healthapp.hospital.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
