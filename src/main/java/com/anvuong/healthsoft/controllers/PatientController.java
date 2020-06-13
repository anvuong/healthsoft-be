package com.anvuong.healthsoft.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.anvuong.healthsoft.exceptions.InvalidUUIDException;
import com.anvuong.healthsoft.exceptions.NoPatientException;
import com.anvuong.healthsoft.exceptions.PatientAlreadyExistsException;
import com.anvuong.healthsoft.models.Patient;
import com.anvuong.healthsoft.services.PatientService;

@CrossOrigin
@RestController
@RequestMapping("/patients")
public class PatientController {
	@Autowired
	private PatientService patientService;

	@PostMapping("")
	public ResponseEntity<?> createPatient(@Valid @RequestBody Patient patient) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(patientService.savePatient(patient));
		} catch (PatientAlreadyExistsException e) {
			HashMap<String, String> map = new HashMap<>();
			map.put("message", "PatientId already exists");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePatient(@PathVariable String id) {
		try {
			patientService.deletePatient(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		} catch (InvalidUUIDException e) {
			HashMap<String, String> map = new HashMap<>();
			map.put("message", "Invalid UUID");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		} catch (NoPatientException e) {
			HashMap<String, String> map = new HashMap<>();
			map.put("message", "No patient found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updatePatient(@PathVariable String id, @Valid @RequestBody Patient patient) {
		try {
			return ResponseEntity.ok(patientService.updatePatient(id, patient));
		} catch (InvalidUUIDException e) {
			HashMap<String, String> map = new HashMap<>();
			map.put("message", "Invalid UUID");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		} catch (PatientAlreadyExistsException e) {
			HashMap<String, String> map = new HashMap<>();
			map.put("message", "Patient with patientId: " + patient.getPatientId() + " exists");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
		} catch (NoPatientException e) {
			HashMap<String, String> map = new HashMap<>();
			map.put("message", "No patient found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
		}
	}

	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
}
