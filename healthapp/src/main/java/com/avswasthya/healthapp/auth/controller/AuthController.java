package com.avswasthya.healthapp.auth.controller;

import com.avswasthya.healthapp.auth.dto.LoginRequest;
import com.avswasthya.healthapp.auth.model.User;
import com.avswasthya.healthapp.auth.service.UserService;
import com.avswasthya.healthapp.doctor.repository.DoctorRepository;
import com.avswasthya.healthapp.patient.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;



@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public AuthController(UserService userService,PatientRepository patientRepository,DoctorRepository doctorRepository) {
        this.userService = userService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        log.info("Health is ok !");
        return "Ok";
    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            // Check if user email already exists
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest().body("User with this email already exists.");
            }

            // Check if patient Aadhar number already exists
            if (user.getPatient() != null && patientRepository.existsByAadharNumber(user.getPatient().getAadharNumber())) {
                return ResponseEntity.badRequest().body("Patient with this Aadhar number already exists.");
            }

            // Check if mobile number already exists
            if (userService.existsMobileNumber(user.getMobileNumber())) {
                return ResponseEntity.badRequest().body("Mobile number already exists.");
            }

            // Check if doctor license already exists
            if (user.getDoctor() != null && doctorRepository.existsDoctorByLicense(user.getDoctor().getLicense())) {
                return ResponseEntity.badRequest().body("Doctor with this license already exists.");
            }

            // Set the user for patient and doctor if they exist
            if (user.getDoctor() != null) {
                user.getDoctor().setUser(user);
            }
            if (user.getPatient() != null) {
                user.getPatient().setUser(user);
            }

            // Save the user
            userService.save(user);

            // Return a successful response
            return ResponseEntity.ok(user.getRole().toString().toLowerCase() + " registered successfully.");
        } catch (Exception e) {
            // Return error response if exception occurs
            return ResponseEntity.badRequest().body("An error occurred during signup: " + e.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        log.info("Login request: " + request.getEmail() + " " + request.getPassword());
        try {
            String result = userService.verify(request);
            return ResponseEntity.ok(result);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        } catch (Exception e) {
            log.error("Login error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.");
        }
    }

}
