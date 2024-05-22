package io.spring.sample.opsstatus.dashboard;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class NoSuchIncidentException extends RuntimeException implements ErrorResponse {

	private final long id;

	public NoSuchIncidentException(long id) {
		super("No incident found with id '%s'".formatted(id));
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	@Override
	public HttpStatusCode getStatusCode() {
		return HttpStatus.NOT_FOUND;
	}

	@Override
	public ProblemDetail getBody() {
		return ProblemDetail.forStatusAndDetail(getStatusCode(), getMessage());
	}

}
