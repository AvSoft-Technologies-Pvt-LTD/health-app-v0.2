package com.avswasthya.healthapp.patient.controller;

import com.avswasthya.healthapp.auth.model.User;
import com.avswasthya.healthapp.auth.repository.UserRepository;
import com.avswasthya.healthapp.auth.service.UserService;
import com.avswasthya.healthapp.patient.model.Patient;
import com.avswasthya.healthapp.patient.repository.PatientRepository;
import com.avswasthya.healthapp.patient.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/patient")
@Slf4j
public class PatientController {


    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    private final PatientService patientService;


    @Autowired
    public PatientController(PatientRepository patientRepository, UserRepository userRepository,  PatientService patientService) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.patientService = patientService;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        log.info("Health is ok!");
        return "This is a health check from PatientController";
    }


    // API to update personal medical details (height, weight, allergies, doesSmoke, doesDrinkAlcohol)
    @PutMapping("/{id}/personalhealth-details")
    public ResponseEntity<Patient> updateMedicalDetails(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        log.info("Updating medical details for patient with ID: {}", id);

        return patientRepository.findById(id)
                .map(patient -> {
                    updates.forEach((key, value) -> {
                        try {
                            switch (key) {
                                case "height" -> patient.setHeight(((Number) value).floatValue());
                                case "weight" -> patient.setWeight(((Number) value).floatValue());
                                case "allergies" -> patient.setAllergy((String) value);
                                case "blood_group" -> patient.setBloodGroup((String) value);
                                case "doesSmoke" -> patient.setDoesSmoke((Boolean) value);
                                case "doesDrinkAlcohol" -> patient.setDoesDrinkAlcohol((Boolean) value);
                                default -> log.warn("Unknown field '{}' for patient ID: {}", key, id);
                            }
                        } catch (ClassCastException e) {
                            log.warn("Invalid type for field '{}' for patient ID: {}", key, id);
                            throw new IllegalArgumentException("Invalid type for field: " + key);
                        }
                    });
                    patientRepository.save(patient);
                    return ResponseEntity.ok(patient);
                })
                .orElseGet(() -> {
                    log.error("Patient not found with ID: {}", id);
                    return ResponseEntity.status(404).body(null);
                });
    }


    // API to update settings
    @PutMapping("/{id}/update")
    public ResponseEntity<String> updatePatient(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("Updating medical details for patient with ID: {}", id);

        if (!userRepository.existsByEmail(updatedUser.getEmail()) || !userRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("User with this email or id doesn't exist.");
        }

        return userRepository.findById(id).map(existingUser -> {
            // Update User fields (except password)
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setMobileNumber(updatedUser.getMobileNumber());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setMobileVerified(updatedUser.isMobileVerified());
            existingUser.setEmailVerified(updatedUser.isEmailVerified());

            // Update Patient fields
            if (existingUser.getPatient() != null && updatedUser.getPatient() != null) {
                Patient existingPatient = existingUser.getPatient();
                Patient updatedPatient = updatedUser.getPatient();

                existingPatient.setFirstName(updatedPatient.getFirstName());
                existingPatient.setMiddleName(updatedPatient.getMiddleName());
                existingPatient.setLastName(updatedPatient.getLastName());
                existingPatient.setHealthCardId(updatedPatient.getHealthCardId());
                existingPatient.setTemporaryAddress(updatedPatient.getTemporaryAddress());
                existingPatient.setBloodGroup(updatedPatient.getBloodGroup());
                existingPatient.setHeight(updatedPatient.getHeight());
                existingPatient.setWeight(updatedPatient.getWeight());
                existingPatient.setDoesSmoke(updatedPatient.getDoesSmoke());
                existingPatient.setDoesDrinkAlcohol(updatedPatient.getDoesDrinkAlcohol());
                existingPatient.setPhoto(updatedPatient.getPhoto());
            }

            userRepository.save(existingUser);
            return ResponseEntity.ok("User and patient details updated successfully (excluding password).");
        }).orElseGet(() -> ResponseEntity.badRequest().body("User not found."));
    }

    // API to update password
    @PutMapping("/{id}/update-password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        log.info("Updating password for user with ID: {}", id);

        return userRepository.findById(id).map(existingUser -> {
            existingUser.setPassword(newPassword); // You can also encrypt it if needed
            userRepository.save(existingUser);
            return ResponseEntity.ok("Password updated successfully.");
        }).orElseGet(() -> ResponseEntity.badRequest().body("User not found."));
    }

    @PutMapping("/generatehealthcard/{id}")
    public ResponseEntity<String> updateHealthCard(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("Updating health card for user with ID: {}", id);

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user = optionalUser.get();

        if (user.getPatient() == null) {
            return ResponseEntity.badRequest().body("No patient record associated with the user.");
        }

        if (user.getPatient().getHealthCardId() == null || user.getPatient().getHealthCardId().isEmpty()) {
            String prefix = updatedUser.getPatient().getHealthCardId(); // This should contain the prefix
            String generatedId = patientService.generateHealthCardId(prefix);

            user.getPatient().setHealthCardId(generatedId);
            userRepository.save(user);

            return ResponseEntity.ok("Health card generated: " + generatedId);
        } else {
            return ResponseEntity.badRequest().body("Health card already exists: " + user.getPatient().getHealthCardId());
        }
    }


}