package io.spring.sample.opsstatus.dashboard;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.spring.sample.opsstatus.availability.InfrastructureComponent;
import io.spring.sample.opsstatus.availability.InfrastructureComponentRepository;
import io.spring.sample.opsstatus.faker.DemoDataGenerator;
import io.spring.sample.opsstatus.incident.Incident;
import io.spring.sample.opsstatus.incident.IncidentRepository;
import io.spring.sample.opsstatus.incident.IncidentStatus;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = DashboardController.class)
class DashboardControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private IncidentRepository incidentRepository;

	@MockBean
	private InfrastructureComponentRepository infrastructureComponentRepository;

	@Test
	void getPopulatesModel() throws Exception {
		Incident inProgress = createTestIncident("Oops", "something failed", IncidentStatus.IN_PROGRESS);
		when(incidentRepository.findAllByStatusOrderByHappenedOnDesc(IncidentStatus.IN_PROGRESS))
			.thenReturn(List.of(inProgress));
		when(incidentRepository.findAllByStatusOrderByHappenedOnDesc(IncidentStatus.SCHEDULED))
			.thenReturn(Collections.emptyList());
		Page<Incident> resolvedIncidents = new PageImpl<>(
				List.of(createTestIncident("Oopsie", "something went wrong", IncidentStatus.RESOLVED)));
		when(incidentRepository.findByStatusOrderByHappenedOnDesc(IncidentStatus.RESOLVED, Pageable.ofSize(5)))
			.thenReturn(resolvedIncidents);
		Set<InfrastructureComponent> infrastructureComponents = new DemoDataGenerator().infrastructureComponents(10);
		when(infrastructureComponentRepository.findAll(Sort.by("name"))).thenReturn(infrastructureComponents);
		mvc.perform(get("/"))
			.andExpect(view().name("dashboard"))
			.andExpect(model().attribute("inProgress", List.of(inProgress)))
			.andExpect(model().attribute("scheduled", List.of()))
			.andExpect(model().attribute("resolved", resolvedIncidents))
			.andExpect(model().attribute("components", infrastructureComponents));
	}

	private Incident createTestIncident(String title, String description, IncidentStatus status) {
		Incident incident = new Incident();
		incident.setTitle(title);
		incident.setDescription(description);
		incident.setHappenedOn(LocalDate.now());
		incident.setStatus(status);
		return incident;
	}

}
