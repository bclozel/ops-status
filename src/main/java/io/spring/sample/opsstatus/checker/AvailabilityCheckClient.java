package io.spring.sample.opsstatus.checker;

import io.spring.sample.opsstatus.availability.Availability;
import io.spring.sample.opsstatus.availability.InfrastructureComponent;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

public class AvailabilityCheckClient {

	private final RestClient client;

	public AvailabilityCheckClient(RestClient.Builder builder, String serviceUrl) {
		this.client = builder.baseUrl(serviceUrl).build();
	}

	public Availability checkAvailability(InfrastructureComponent component) {

		try {
			ResponseEntity<AvailabilityResponse> response = this.client.get()
				.uri(component.getCheckUri())
				.accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + component.getCheckToken())
				.retrieve()
				.toEntity(AvailabilityResponse.class);
			return getAvailability(response.getBody());
		}
		catch (RestClientException ex) {
			return Availability.UNAVAILABLE;
		}

	}

	private Availability getAvailability(AvailabilityResponse response) {
		if (response == null) {
			return Availability.UNKNOWN;
		}
		return response.toAvailability();
	}

	record AvailabilityResponse(String status) {

		private Availability toAvailability() {
			if ("OK".equals(status)) {
				return Availability.OPERATIONAL;
			}
			else {
				return Availability.UNKNOWN;
			}
		}

	}

}
