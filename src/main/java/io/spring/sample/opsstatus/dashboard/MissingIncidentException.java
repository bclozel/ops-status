package io.spring.sample.opsstatus.dashboard;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MissingIncidentException extends RuntimeException {

	public MissingIncidentException(Long id) {
		super("Could not find an incident with id '%s'.".formatted(id));
	}

}
