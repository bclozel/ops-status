package io.spring.sample.opsstatus;

import java.time.LocalDateTime;

import io.spring.sample.opsstatus.availability.Availability;
import io.spring.sample.opsstatus.availability.InfrastructureComponent;
import io.spring.sample.opsstatus.availability.InfrastructureComponentRepository;
import io.spring.sample.opsstatus.checker.AvailabilityCheckClient;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityChecker {

	private final AvailabilityCheckClient client;

	private final InfrastructureComponentRepository repository;

	public AvailabilityChecker(AvailabilityCheckClient client, InfrastructureComponentRepository repository) {
		this.client = client;
		this.repository = repository;
	}

	@Scheduled(fixedRate = 5000)
	public void checkComponents() {
		Iterable<InfrastructureComponent> components = this.repository.findAll();
		for (InfrastructureComponent component : components) {
			Availability availability = this.client.checkAvailability(component);
			component.setAvailability(availability);
			component.setLastCheckedOn(LocalDateTime.now());
		}
		this.repository.saveAll(components);
	}

}
