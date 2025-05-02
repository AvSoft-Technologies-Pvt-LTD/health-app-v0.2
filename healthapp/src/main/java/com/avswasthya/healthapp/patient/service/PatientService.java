package com.avswasthya.healthapp.patient.service;

import com.avswasthya.healthapp.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PatientService {

    private final Random rand = new Random();
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public String generateHealthCardId(String prefix) {
        prefix = prefix.toUpperCase().trim();
        int attempts = 0;
        int maxAttempts = 5;

        if (prefix.length() < 8) {
            throw new IllegalArgumentException("Invalid prefix format. Expected at least 8 characters.");
        }

        while (attempts < maxAttempts) {
            String randomNumber = String.format("%09d", rand.nextInt(1_000_000_000));
            String fullHealthCardId = prefix + randomNumber;

            boolean exists = patientRepository.existsByHealthCardId(fullHealthCardId);
            if (!exists) {
                return fullHealthCardId;
            }

            attempts++;
        }


        throw new RuntimeException("Failed to generate unique Health Card ID after " + maxAttempts + " attempts");
    }
}
