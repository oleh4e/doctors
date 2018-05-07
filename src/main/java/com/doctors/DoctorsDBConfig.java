package com.doctors;

import com.doctors.doctor.Appointment;
import com.doctors.doctor.Doctor;
import com.doctors.doctor.JpaDoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DoctorsDBConfig {

    @Bean
    CommandLineRunner initDoctorDb(JpaDoctorRepository repository) {
        return args -> {
            if (!repository.findAll().isEmpty()) {
                return;
            }

            Set<String> specializations = new HashSet<String>() {{
                add("Surgeon");
                add("Dentist");
            }};

            Set<Appointment> appointments = new HashSet<Appointment>() {{
                add(new Appointment(LocalDate.now(), LocalTime.of(9, 0), 8));
            }};

            repository.save(new Doctor("Alex", specializations, appointments));
        };
    }
}
