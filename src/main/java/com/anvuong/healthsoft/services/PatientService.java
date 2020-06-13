package com.anvuong.healthsoft.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anvuong.healthsoft.models.Patient;
import com.anvuong.healthsoft.repositories.PatientRepository;

@Service("patientService")
public class PatientService {
	@Autowired
	private PatientRepository patientRepository;

	public Patient savePatient(Patient patient) {
		return patientRepository.save(patient);
	}
}
