package com.avswasthya.healthapp.patient.controller;

import com.avswasthya.healthapp.patient.model.FamilyHistory;
import com.avswasthya.healthapp.patient.model.Patient;
import com.avswasthya.healthapp.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient/")
public class FamilyHistoryController {

    private final PatientRepository patientRepository;

    @Autowired
    public FamilyHistoryController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // POST - Create FamilyHistory
    @PostMapping("familyhistory/{id}")
    public ResponseEntity<String> createFamilyHistory(@PathVariable long id, @RequestBody FamilyHistory familyHistory) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.getFamilyHistories().add(familyHistory);
            patientRepository.save(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body("FamilyHistory Saved");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
    }

    // GET - Get all FamilyHistory records of a patient
    @GetMapping("familyhistory/{id}")
    public ResponseEntity<?> getFamilyHistory(@PathVariable long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            List<FamilyHistory> familyHistories = patient.getFamilyHistories();
            return ResponseEntity.ok(familyHistories);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
    }

    // PUT - Update the whole FamilyHistory list of a patient
    @PutMapping("familyhistory/{id}")
    public ResponseEntity<String> updateFamilyHistory(@PathVariable long id, @RequestBody List<FamilyHistory> familyHistories) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setFamilyHistories(familyHistories); // Overwrite old histories with new ones
            patientRepository.save(patient);
            return ResponseEntity.ok("FamilyHistory updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
    }
}
