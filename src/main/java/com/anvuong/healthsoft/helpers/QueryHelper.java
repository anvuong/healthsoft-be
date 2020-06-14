package com.anvuong.healthsoft.helpers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class QueryHelper {
	final Map<String, String> queryParamToTableColumnMap;

	public QueryHelper() {
		queryParamToTableColumnMap = new HashMap<>();
		queryParamToTableColumnMap.put("lastname", "lastName");
		queryParamToTableColumnMap.put("firstname", "firstName");
		queryParamToTableColumnMap.put("gender", "gender");
		queryParamToTableColumnMap.put("patientid", "patientId");
		queryParamToTableColumnMap.put("dob", "dob");
		queryParamToTableColumnMap.put("withdeleted", "softDeleted");
	}

	public String getColumnName(String queryName) {
		return queryParamToTableColumnMap.get(queryName);
	}
}
