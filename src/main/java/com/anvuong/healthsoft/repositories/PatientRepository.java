package com.anvuong.healthsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anvuong.healthsoft.models.Patient;

@Repository("patientRepository")
public interface PatientRepository extends JpaRepository<Patient, String> {

}
