package io.spring.sample.opsstatus.dashboard;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class MissingIncidentException extends ErrorResponseException {

	public MissingIncidentException(Long id) {
		super(HttpStatus.NOT_FOUND, ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
				"Could not find an incident with id '%s'.".formatted(id)), null);
	}

}
