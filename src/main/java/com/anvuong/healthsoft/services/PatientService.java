package com.anvuong.healthsoft.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anvuong.healthsoft.exceptions.PatientAlreadyExistsException;
import com.anvuong.healthsoft.models.Patient;
import com.anvuong.healthsoft.repositories.PatientRepository;

@Service("patientService")
public class PatientService {
	@Autowired
	private PatientRepository patientRepository;

	public Patient savePatient(Patient patient) throws PatientAlreadyExistsException {
		final var existingPatient = patientRepository.findByPatientId(patient.getPatientId());
		if (existingPatient == null) {
			return patientRepository.save(patient);
		}
		if (!existingPatient.isSoftDeleted()) {
			throw new PatientAlreadyExistsException();
		}
		existingPatient.setSoftDeleted(false);
		existingPatient.setFirstName(patient.getFirstName());
		existingPatient.setMiddleName(patient.getMiddleName());
		existingPatient.setLastName(patient.getLastName());
		existingPatient.setDob(patient.getDob());
		existingPatient.setGender(patient.getGender());
		return patientRepository.save(existingPatient);
	}
}
