package com.avswasthya.healthapp.doctor.repository;

import com.avswasthya.healthapp.doctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {


    boolean existsDoctorByLicense(String license);

    Optional<Doctor> findById(Long id);
}
