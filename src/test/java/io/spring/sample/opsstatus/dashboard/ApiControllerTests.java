package io.spring.sample.opsstatus.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import io.spring.sample.opsstatus.incident.Incident;
import io.spring.sample.opsstatus.incident.IncidentOrigin;
import io.spring.sample.opsstatus.incident.IncidentRepository;
import io.spring.sample.opsstatus.incident.IncidentStatus;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApiController.class)
class ApiControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private IncidentRepository incidentRepository;

	@Test
	void incidentsWhenNoIncident() throws Exception {
		when(this.incidentRepository.streamAll()).thenReturn(Stream.empty());
		mvc.perform(get("/api/incidents")).andExpect(status().isOk())
				.andExpect(jsonPath("incidents").isArray()).andExpect(jsonPath("incidents").isEmpty());
	}

	@Test
	void incidentsWithIncidents() throws Exception {
		List<Incident> incidents = List.of(
				createTestIncident("test", "a description", LocalDate.of(2024, 1, 2), IncidentOrigin.MAINTENANCE,
						IncidentStatus.RESOLVED),
				createTestIncident("another", "another description", LocalDate.of(2024, 1, 20), IncidentOrigin.ISSUE,
						IncidentStatus.IN_PROGRESS));
		when(this.incidentRepository.streamAll()).thenReturn(incidents.stream());
		mvc.perform(get("/api/incidents"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("incidents").isArray())
			.andExpect(jsonPath("incidents[0]").value(jsonIncidentMatcher(incidents.get(0))))
			.andExpect(jsonPath("incidents[1]").value(jsonIncidentMatcher(incidents.get(1))));
	}

	@Test
	void incident() throws Exception {
		Incident testIncident = createTestIncident("test", "a description", LocalDate.of(2024, 1, 2),
				IncidentOrigin.MAINTENANCE, IncidentStatus.RESOLVED);
		when(this.incidentRepository.findById(42L)).thenReturn(Optional.of(testIncident));
		mvc.perform(get("/api/incidents/42"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(jsonIncidentMatcher(testIncident)));
	}

	@Test
	void incidentWithNoSuchIncident() throws Exception {
		when(this.incidentRepository.findById(42L)).thenReturn(Optional.empty());
		mvc.perform(get("/api/incidents/42").accept(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
				.andExpect(content().json(readJson(new ClassPathResource("missing-incident-42.json", getClass())),true));
	}

	private Matcher<?> jsonIncidentMatcher(Incident incident) {
		return Matchers.allOf(hasEntry("title", incident.getTitle()),
				hasEntry("description", incident.getDescription()),
				hasEntry("happenedOn", incident.getHappenedOn().toString()),
				hasEntry("origin", incident.getOrigin().toString()),
				hasEntry("status", incident.getStatus().toString()));
	}

	private String readJson(Resource resource) {
		try {
			try (InputStream in = resource.getInputStream()) {
				return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
			}
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	private Incident createTestIncident(String title, String description, LocalDate happenedOn, IncidentOrigin origin,
			IncidentStatus status) {
		Incident incident = new Incident();
		incident.setTitle(title);
		incident.setDescription(description);
		incident.setHappenedOn(happenedOn);
		incident.setOrigin(origin);
		incident.setStatus(status);
		return incident;
	}

}
