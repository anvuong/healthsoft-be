package com.anvuong.healthsoft.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anvuong.healthsoft.models.Patient;
import com.anvuong.healthsoft.services.PatientService;

@CrossOrigin
@RestController
@RequestMapping("/patients")
public class PatientController {
	@Autowired
	private PatientService patientService;

	@PostMapping("")
	public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
		return ResponseEntity.ok(patientService.savePatient(patient));
	}
}
