package io.spring.sample.opsstatus.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.spring.sample.opsstatus.incident.Incident;
import io.spring.sample.opsstatus.incident.IncidentOrigin;
import io.spring.sample.opsstatus.incident.IncidentRepository;
import io.spring.sample.opsstatus.incident.IncidentStatus;
import io.spring.sample.opsstatus.incident.ItemType;
import io.spring.sample.opsstatus.incident.ReportItem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

	private final IncidentRepository repository;

	public ApiController(IncidentRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/incidents")
	public IncidentsDescriptor incidents() {
		return new IncidentsDescriptor(this.repository.streamAll().map(IncidentInfo::fromIncident).toList());
	}

	@GetMapping("/incidents/{id}")
	public IncidentInfo incident(@PathVariable Long id) {
		return this.repository.findById(id)
			.map(IncidentInfo::fromIncident)
			.orElseThrow(() -> new MissingIncidentException(id));
	}

	public record IncidentsDescriptor(List<IncidentInfo> incidents) {
	}

	public record IncidentInfo(Long id, String title, String description, LocalDate happenedOn, IncidentOrigin origin,
			IncidentStatus status, List<IncidentUpdate> updates) {

		static IncidentInfo fromIncident(Incident incident) {
			return new IncidentInfo(incident.getId(), incident.getTitle(), incident.getDescription(),
					incident.getHappenedOn(), incident.getOrigin(), incident.getStatus(),
					incident.getReportItems().stream().map(IncidentUpdate::fromReportItem).toList());
		}

	}

	public record IncidentUpdate(ItemType type, LocalDateTime date, String description) {

		static IncidentUpdate fromReportItem(ReportItem item) {
			return new IncidentUpdate(item.getType(), item.getDate(), item.getDescription());
		}
	}

}