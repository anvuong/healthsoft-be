package com.anvuong.healthsoft.models.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.anvuong.healthsoft.models.Patient;

public class PatientSpecificationsBuilder {
	private final List<SearchCriteria> criteriaList;
	private final boolean combinedByOr;

	public PatientSpecificationsBuilder(String search) {
		criteriaList = new ArrayList<>();
		combinedByOr = search != null && "true".equalsIgnoreCase(search);
	}

	public Specification<Patient> build() {
		final var criteriaListSize = criteriaList.size();
		if (criteriaListSize < 1) {
			return null;
		}
		final var spec1 = new PatientSpecification(criteriaList.get(0));
		if (criteriaListSize == 1) {
			return spec1;
		}
		var specs = Specification.where(spec1);
		if (combinedByOr) {
			for (int i = 1; i < criteriaListSize; i++) {
				specs = specs.or(new PatientSpecification(criteriaList.get(i)));
			}
		} else {
			for (int i = 1; i < criteriaListSize; i++) {
				specs = specs.and(new PatientSpecification(criteriaList.get(i)));
			}
		}
		return specs;
	}

	public final PatientSpecificationsBuilder with(final SearchCriteria criteria) { 
		criteriaList.add(criteria);
	    return this;
	}
}
