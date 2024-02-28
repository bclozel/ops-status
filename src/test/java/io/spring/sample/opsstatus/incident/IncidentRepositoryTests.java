package io.spring.sample.opsstatus.incident;

import java.time.LocalDate;
import java.util.List;

import io.spring.sample.opsstatus.faker.DemoDataGenerator;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
class IncidentRepositoryTests {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.6-alpine");

	private final IncidentRepository repository;

	private final DemoDataGenerator demoDataGenerator;

	IncidentRepositoryTests(@Autowired IncidentRepository repository) {
		this.repository = repository;
		this.demoDataGenerator = new DemoDataGenerator();
	}

	@Test
	void createIncident() {
		List<Incident> incidents = this.demoDataGenerator.generatePastIncidents(5);
		assertThat(incidents).allSatisfy(ic -> assertThat(ic.getId()).isNull());
		this.repository.saveAll(incidents);
		assertThat(incidents).allSatisfy(ic -> assertThat(ic.getId()).isNotNull());
	}

	@Test
	void streamAllSortIncidentByHappenedOnField() {
		List<Incident> incidents = List.of(createIncident(LocalDate.of(2024, 2, 7), IncidentStatus.RESOLVED),
				createIncident(LocalDate.of(2024, 2, 25), IncidentStatus.IN_PROGRESS),
				createIncident(LocalDate.of(2024, 2, 9), IncidentStatus.RESOLVED),
				createIncident(LocalDate.of(2024, 2, 17), IncidentStatus.RESOLVED));
		this.repository.saveAll(incidents);
		assertThat(this.repository.streamAll()).extracting(Incident::getHappenedOn)
			.containsExactly(LocalDate.of(2024, 2, 25), LocalDate.of(2024, 2, 17), LocalDate.of(2024, 2, 9),
					LocalDate.of(2024, 2, 7));
	}

	@Test
	void findAllByStatusOrderByHappenedOnDesc() {
		List<Incident> incidents = List.of(createIncident(LocalDate.of(2024, 2, 7), IncidentStatus.RESOLVED),
				createIncident(LocalDate.of(2024, 2, 25), IncidentStatus.IN_PROGRESS),
				createIncident(LocalDate.of(2024, 2, 9), IncidentStatus.RESOLVED),
				createIncident(LocalDate.of(2024, 2, 17), IncidentStatus.RESOLVED));
		this.repository.saveAll(incidents);
		assertThat(this.repository.findAllByStatusOrderByHappenedOnDesc(IncidentStatus.RESOLVED))
			.extracting(Incident::getHappenedOn)
			.containsExactly(LocalDate.of(2024, 2, 17), LocalDate.of(2024, 2, 9), LocalDate.of(2024, 2, 7));
	}

	@Test
	void findByStatusOrderByHappenedOnDesc() {
		List<Incident> incidents = List.of(createIncident(LocalDate.of(2024, 2, 7), IncidentStatus.RESOLVED),
				createIncident(LocalDate.of(2024, 2, 25), IncidentStatus.IN_PROGRESS),
				createIncident(LocalDate.of(2024, 2, 9), IncidentStatus.RESOLVED),
				createIncident(LocalDate.of(2024, 2, 17), IncidentStatus.RESOLVED));
		this.repository.saveAll(incidents);
		Slice<Incident> slice = this.repository.findByStatusOrderByHappenedOnDesc(IncidentStatus.RESOLVED,
				Pageable.ofSize(2));
		assertThat(slice.getSize()).isEqualTo(2);
		assertThat(slice.hasNext()).isTrue();
		assertThat(slice.getContent()).extracting(Incident::getHappenedOn)
			.containsExactly(LocalDate.of(2024, 2, 17), LocalDate.of(2024, 2, 9));
	}

	private Incident createIncident(LocalDate happenedOn, IncidentStatus status) {
		Incident incident = new Incident();
		incident.setTitle("oops");
		incident.setDescription("oops oops");
		incident.setHappenedOn(happenedOn);
		incident.setStatus(status);
		return incident;
	}

}
