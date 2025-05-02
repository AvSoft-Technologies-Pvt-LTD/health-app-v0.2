package com.avswasthya.healthapp.appointment.controller;

import com.avswasthya.healthapp.appointment.model.Appointment;
import com.avswasthya.healthapp.appointment.service.AppointmentService;
import com.avswasthya.healthapp.doctor.model.Doctor;
import com.avswasthya.healthapp.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, DoctorRepository doctorRepository) {
        this.appointmentService = appointmentService;
        this.doctorRepository = doctorRepository;
    }

    // 1. Book appointment
    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestBody Appointment appointment) {
        if (appointment == null || appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            return ResponseEntity.badRequest().body("Invalid appointment or doctor information.");
        }

        Optional<Doctor> optionalDoctor = doctorRepository.findById(appointment.getDoctor().getId());
        if (optionalDoctor.isEmpty()) {
            return ResponseEntity.notFound().build(); // Doctor not found
        }

        Doctor doctor = optionalDoctor.get();

        if (doctor.isFreelancer()) {
            appointment.setHospital(null); // Freelancers should not have hospital
        } else if (appointment.getHospital() == null || appointment.getHospital().getId() == null) {
            return ResponseEntity.badRequest().body("Hospital is required for non-freelancer doctors.");
        }

        appointmentService.bookAppointment(appointment);
        return ResponseEntity.ok("Appointment booked successfully.");
    }

    // 2. Get patient appointments
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForPatient(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    // 3. Get doctor appointments
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForDoctor(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    // 4. Cancel or update appointment status
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status // example: CANCELLED, COMPLETED
    ) {
        appointmentService.updateStatus(id, status);
        return ResponseEntity.ok("Appointment status updated.");
    }
}
