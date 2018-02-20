package com.doctors;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DoctorsDBConfig {

    /*@Bean
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
    }*/
}
