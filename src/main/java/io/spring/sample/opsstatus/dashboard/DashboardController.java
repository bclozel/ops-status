package io.spring.sample.opsstatus.dashboard;

import io.spring.sample.opsstatus.availability.InfrastructureComponentRepository;
import io.spring.sample.opsstatus.incident.IncidentRepository;
import io.spring.sample.opsstatus.incident.IncidentStatus;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

	private final IncidentRepository incidentRepository;

	private final InfrastructureComponentRepository infrastructureComponentRepository;

	public DashboardController(IncidentRepository incidentRepository,
			InfrastructureComponentRepository infrastructureComponentRepository) {
		this.incidentRepository = incidentRepository;
		this.infrastructureComponentRepository = infrastructureComponentRepository;
	}

	@GetMapping("/")
	public String dashboard(Model model) {
		model.addAttribute("inProgress",
				this.incidentRepository.findAllByStatusOrderByHappenedOnDesc(IncidentStatus.IN_PROGRESS));
		model.addAttribute("scheduled",
				this.incidentRepository.findAllByStatusOrderByHappenedOnDesc(IncidentStatus.SCHEDULED));
		model.addAttribute("resolved",
				this.incidentRepository.findByStatusOrderByHappenedOnDesc(IncidentStatus.RESOLVED, Pageable.ofSize(5)));

		model.addAttribute("components", this.infrastructureComponentRepository.findAll(Sort.by("name")));
		return "dashboard";
	}

}
