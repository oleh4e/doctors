package com.doctors.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaDoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findById(Integer id);

    Page<Doctor> findBySpecializationsInAndName(List<String> specializations, String name, Pageable pageable);

    Page<Doctor> findBySpecializationsIgnoreCaseIn(List<String> specializations, Pageable sort);

    Page<Doctor> findByNameIgnoreCase(String name, Pageable sort);


}
