package com.doctors.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaDoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findById(Integer id);

    List<Doctor> findBySpecializationsInAndName(List<String> specializations, String name);

    List<Doctor> findBySpecializationsIgnoreCaseIn(List<String> specializations);

    List<Doctor> findByNameIgnoreCase(String name);


}
