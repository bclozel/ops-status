package io.spring.sample.opsstatus.checker;

import java.util.List;

import io.spring.sample.opsstatus.availability.Availability;
import io.spring.sample.opsstatus.availability.InfrastructureComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.web.client.RestClient;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@TestInstance(Lifecycle.PER_CLASS)
class AvailabilityCheckClientTests {

	private static final String RESPONSE_OK = """
			{ "status": "OK" }
			""";

	private static final String RESPONSE_UNEXPECTED = """
			{ "name": "unexpected" }
			""";

	private static final String RESPONSE_DOWN = """
			{ "status": "DOWN" }
			""";

	private final AvailabilityCheckClient client;

	private final MockRestServiceServer server;

	AvailabilityCheckClientTests(@Autowired RestClient.Builder restClientBuilder,
			@Autowired MockRestServiceServer server) {
		this.client = new AvailabilityCheckClient(restClientBuilder, "https://example.com");
		this.server = server;
	}

	@Test
	void checkAvailabilitySuccess() {
		InfrastructureComponent component = new InfrastructureComponent();
		component.setCheckUri("/test/availability");
		component.setCheckToken("test-123");
		this.server.expect(prepareRequest("/test/availability", "test-123"))
			.andRespond(withSuccess(RESPONSE_OK, MediaType.APPLICATION_JSON));
		assertThat(client.checkAvailability(component)).isEqualTo(Availability.OPERATIONAL);
	}

	@Test
	void checkAvailabilitySuccessWithUnexpectedStatus() {
		InfrastructureComponent component = new InfrastructureComponent();
		component.setCheckUri("/test/availability");
		component.setCheckToken("test-123");
		this.server.expect(prepareRequest("/test/availability", "test-123"))
			.andRespond(withSuccess(RESPONSE_UNEXPECTED, MediaType.APPLICATION_JSON));
		assertThat(client.checkAvailability(component)).isEqualTo(Availability.UNKNOWN);
	}

	@Test
	void checkAvailabilitySuccessWithNoStatus() {
		InfrastructureComponent component = new InfrastructureComponent();
		component.setCheckUri("/test/availability");
		component.setCheckToken("test-123");
		this.server.expect(prepareRequest("/test/availability", "test-123")).andRespond(withStatus(HttpStatus.OK));
		assertThat(client.checkAvailability(component)).isEqualTo(Availability.UNKNOWN);
	}

	@Test
	void checkAvailabilityFailure() {
		InfrastructureComponent component = new InfrastructureComponent();
		component.setCheckUri("/broken/availability");
		component.setCheckToken("test-123");
		this.server.expect(prepareRequest("/broken/availability", "test-123"))
			.andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE).body(RESPONSE_DOWN));
		assertThat(client.checkAvailability(component)).isEqualTo(Availability.UNAVAILABLE);
	}

	public static RequestMatcher prepareRequest(String path, String token) {
		return (request) -> {
			assertThat(request.getURI()).hasPath(path);
			assertThat(request.getHeaders()).contains(
					entry(HttpHeaders.ACCEPT, List.of(MediaType.APPLICATION_JSON_VALUE)),
					entry(HttpHeaders.AUTHORIZATION, List.of("Bearer %s".formatted(token))));
		};
	}

}
