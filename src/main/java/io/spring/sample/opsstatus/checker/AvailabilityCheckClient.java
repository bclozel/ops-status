package io.spring.sample.opsstatus.checker;

import io.spring.sample.opsstatus.availability.Availability;
import io.spring.sample.opsstatus.availability.InfrastructureComponent;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class AvailabilityCheckClient {

	private final RestTemplate client;

	public AvailabilityCheckClient(RestTemplateBuilder builder, String serviceUrl) {
		this.client = builder.rootUri(serviceUrl).build();
	}

	public Availability checkAvailability(InfrastructureComponent component) {

		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		requestHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + component.getCheckToken());
		HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);

		try {
			ResponseEntity<AvailabilityResponse> response = this.client.exchange(component.getCheckUri(),
					HttpMethod.GET, requestEntity, AvailabilityResponse.class);
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
