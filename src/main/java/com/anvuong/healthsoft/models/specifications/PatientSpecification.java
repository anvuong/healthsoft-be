package com.anvuong.healthsoft.models.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.anvuong.healthsoft.models.Patient;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PatientSpecification implements Specification<Patient> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SearchCriteria criteria;

	@Override
	public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		if (criteria == null) {
			return null;
		}
		if (criteria.getOperation().equalsIgnoreCase(":")) {
			final var key = criteria.getKey();
			final var value = criteria.getValue();
			if ("lastName".equals(key) || "firstName".equals(key)) {
				return builder.like(builder.lower(root.<String>get(key)), value.toString().toLowerCase() + "%");
			}
			if ("gender".equals(key) || "patientId".equals(key)) {
				return builder.equal(builder.lower(root.<String>get(key)), value.toString().toLowerCase());
			}
			if ("dob".equals(key)) {
				return builder.equal(root.<String>get(key), value);
			}
			if ("softDeleted".equals(key)) {
				return builder.equal(root.<Boolean>get(key), Boolean.parseBoolean(value.toString()));
			}
        }
		return null;
	}

}
