package io.spring.sample.opsstatus;

import io.spring.sample.opsstatus.availability.InfrastructureComponentRepository;
import io.spring.sample.opsstatus.faker.DemoDataGenerator;
import io.spring.sample.opsstatus.incident.IncidentRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
class RandomDataGenerator implements ApplicationRunner {

	private final IncidentRepository incidentRepository;

	private final InfrastructureComponentRepository infrastructureComponentRepository;

	private final DemoDataGenerator demoDataGenerator;

	RandomDataGenerator(IncidentRepository incidentRepository,
			InfrastructureComponentRepository infrastructureComponentRepository) {
		this.incidentRepository = incidentRepository;
		this.infrastructureComponentRepository = infrastructureComponentRepository;
		this.demoDataGenerator = new DemoDataGenerator();
	}

	@Override
	public void run(ApplicationArguments args) {
		if (this.incidentRepository.count() == 0) {
			this.incidentRepository.saveAll(this.demoDataGenerator.generatePastIncidents(10));
			this.incidentRepository.save(this.demoDataGenerator.generateMaintenanceUpdate());
			this.infrastructureComponentRepository.saveAll(this.demoDataGenerator.infrastructureComponents(5));
		}
	}

}
