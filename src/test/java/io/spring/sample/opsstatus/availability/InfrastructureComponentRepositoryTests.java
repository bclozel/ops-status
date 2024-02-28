package io.spring.sample.opsstatus.availability;

import java.time.LocalDateTime;

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

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
class InfrastructureComponentRepositoryTests {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.6-alpine");

	private final InfrastructureComponentRepository repository;

	private final DemoDataGenerator demoDataGenerator;

	InfrastructureComponentRepositoryTests(@Autowired InfrastructureComponentRepository repository) {
		this.repository = repository;
		this.demoDataGenerator = new DemoDataGenerator();
	}

	@Test
	void createInfrastructureComponent() {
		InfrastructureComponent ic = createTestInfrastructureComponent();
		assertThat(ic.getId()).isNull();
		InfrastructureComponent savedIc = this.repository.save(ic);
		assertThat(savedIc.getId()).isNotNull();
	}

	@Test
	void addUnavailableOutage() {
		InfrastructureComponent ic = createTestInfrastructureComponent();
		ic.addOutage(startOutage(Availability.UNAVAILABLE));
		ic.setAvailability(Availability.UNAVAILABLE);
		InfrastructureComponent savedIc = this.repository.save(ic);
		assertThat(savedIc.isUnavailable()).isTrue();
		assertThat(this.repository.findById(savedIc.getId()))
			.hasValueSatisfying(component -> assertThat(component.getOutages()).singleElement().satisfies(outage -> {
				assertThat(outage.getAvailability()).isEqualTo(Availability.UNAVAILABLE);
				assertThat(outage.getEndedOn()).isNull();
			}));
	}

	@Test
	void addPartialOutage() {
		InfrastructureComponent ic = createTestInfrastructureComponent();
		ic.addOutage(startOutage(Availability.PARTIAL));
		ic.setAvailability(Availability.PARTIAL);
		InfrastructureComponent savedIc = this.repository.save(ic);
		assertThat(savedIc.isPartiallyAvailable()).isTrue();
	}

	@Test
	void resolveOutage() {
		InfrastructureComponent ic = createTestInfrastructureComponent();
		ic.addOutage(startOutage(Availability.UNAVAILABLE));
		ic.setAvailability(Availability.UNAVAILABLE);
		InfrastructureComponent savedIc = this.repository.save(ic);
		Outage outage = savedIc.getOutages().get(0);
		outage.setEndedOn(LocalDateTime.now().plusHours(30));
		outage.setAvailability(Availability.OPERATIONAL);
		ic.setAvailability(Availability.OPERATIONAL);
		InfrastructureComponent finalIc = this.repository.save(savedIc);
		assertThat(finalIc.isOperational()).isTrue();
	}

	private InfrastructureComponent createTestInfrastructureComponent() {
		return demoDataGenerator.infrastructureComponents(1).iterator().next();
	}

	private Outage startOutage(Availability availability) {
		Outage outage = new Outage();
		outage.setAvailability(availability);
		outage.setStartedOn(LocalDateTime.now());
		return outage;
	}

}
