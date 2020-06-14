package com.anvuong.healthsoft.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.anvuong.healthsoft.exceptions.InvalidUUIDException;
import com.anvuong.healthsoft.exceptions.NoPatientException;
import com.anvuong.healthsoft.exceptions.PatientAlreadyExistsException;
import com.anvuong.healthsoft.helpers.QueryHelper;
import com.anvuong.healthsoft.helpers.UUIDHelper;
import com.anvuong.healthsoft.models.Patient;
import com.anvuong.healthsoft.models.specifications.PatientSpecificationsBuilder;
import com.anvuong.healthsoft.models.specifications.SearchCriteria;
import com.anvuong.healthsoft.repositories.PatientRepository;

@Service("patientService")
public class PatientService {
	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private UUIDHelper uuidHelper;

	@Autowired
	private QueryHelper queryHelper;

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

	public Patient updatePatient(String id, Patient patient) throws InvalidUUIDException, NoPatientException, PatientAlreadyExistsException {
		uuidHelper.ensureValidUUID(id);
		final var existingPatient = patientRepository.findById(id).orElse(null);
		if (existingPatient == null) {
			throw new NoPatientException();
		}
		final var patientId = patient.getPatientId();
		if (patientId != null && !existingPatient.getPatientId().equals(patientId)) {
			final var patientWithNewId = patientRepository.findByPatientId(patientId);
			if (patientWithNewId != null) {
				throw new PatientAlreadyExistsException();
			}
			existingPatient.setPatientId(patientId);
		}
		existingPatient.setLastName(patient.getLastName());
		existingPatient.setFirstName(patient.getFirstName());
		existingPatient.setMiddleName(patient.getMiddleName());
		existingPatient.setDob(patient.getDob());
		existingPatient.setGender(patient.getGender());
		patientRepository.save(existingPatient);
		return existingPatient;
	}

	public Patient getPatient(String id) throws InvalidUUIDException, NoPatientException {
		uuidHelper.ensureValidUUID(id);
		final var existingPatient = patientRepository.findById(id).orElse(null);
		if (existingPatient == null) {
			throw new NoPatientException();
		}
		return existingPatient;
	}

	public List<Patient> getPatients(Map<String, String> filterParams) {
		final var specsBuilder = new PatientSpecificationsBuilder(filterParams.get("search"));
		final var value = filterParams.get("withDeleted");
		if (value != null && "true".equals(value.toLowerCase())) {
			specsBuilder.with(new SearchCriteria("softDeleted", ":", value));
		} else {
			specsBuilder.with(new SearchCriteria("softDeleted", ":", "false"));
		}
		final var sortDirection = filterParams.get("sort");
		final var isSortAsc = sortDirection != null && "asc".equals(sortDirection.toLowerCase()) ? true : false;
		Sort sort = null;
		final var keys = new ArrayList<String>(filterParams.keySet());
		for (String key : keys) {
			final var columnName = queryHelper.getColumnName(key.toLowerCase());
			if (columnName != null) {
				if (!"softDeleted".equals(columnName)) {
					specsBuilder.with(new SearchCriteria(columnName, ":", filterParams.get(key)));
				}
				if (isSortAsc) {
					sort = sort == null ? Sort.by(columnName).ascending() : sort.and(Sort.by(columnName).ascending());
				} else {
					sort = sort == null ? Sort.by(columnName).descending() : sort.and(Sort.by(columnName).descending());
				}
			}
		}
		if (sort == null) {
			sort = isSortAsc ? Sort.by("patientId").ascending() : Sort.by("patientId").descending();
		}
		return patientRepository.findAll(specsBuilder.build(), sort);
	}

	public void deletePatient(String id) throws InvalidUUIDException, NoPatientException {
		uuidHelper.ensureValidUUID(id);
		final var patient = patientRepository.findById(id).orElse(null);
		if (patient == null) {
			throw new NoPatientException();
		}
		patient.setSoftDeleted(true);
		patientRepository.save(patient);
	}
}
