package com.anvuong.healthsoft.helpers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.anvuong.healthsoft.exceptions.InvalidUUIDException;

@Component
public class UUIDHelper {
	public void ensureValidUUID(String uuid) throws InvalidUUIDException {
		try {
			UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			throw new InvalidUUIDException();
		}
	}
}
