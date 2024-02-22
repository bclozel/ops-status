package io.spring.sample.opsstatus.faker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.spring.sample.opsstatus.availability.Availability;
import io.spring.sample.opsstatus.availability.InfrastructureComponent;
import io.spring.sample.opsstatus.incident.Incident;
import io.spring.sample.opsstatus.incident.IncidentOrigin;
import io.spring.sample.opsstatus.incident.IncidentStatus;
import io.spring.sample.opsstatus.incident.ItemType;
import io.spring.sample.opsstatus.incident.ReportItem;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

public class DemoDataGenerator {

	private final DataFaker faker = new DataFaker();

	public List<Incident> generatePastIncidents(int count) {
		List<Incident> incidents = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			LocalDate pastIncidentDate = this.faker.onlineGaming().pastIncidentDate();
			Incident incident = new Incident();
			String[] split = this.faker.onlineGaming().issue().split(" \\| ");
			incident.setTitle(split[0]);
			incident.setDescription(split[1]);
			incident.setStatus(IncidentStatus.RESOLVED);
			incident.setHappenedOn(pastIncidentDate);
			incident.setOrigin(IncidentOrigin.ISSUE);
			ReportItem update = ReportItem.create(ItemType.INVESTIGATING, this.faker.onlineGaming().incidentUpdate());
			update.setDate(this.faker.onlineGaming().reportItemTimestamp(pastIncidentDate));
			incident.addReportItem(update);
			ReportItem resolution = ReportItem.create(ItemType.RESOLVED, this.faker.onlineGaming().incidentUpdate());
			update.setDate(this.faker.onlineGaming().reportItemTimestamp(pastIncidentDate));
			incident.addReportItem(resolution);
			incidents.add(incident);
		}
		return incidents;
	}

	public Incident generateMaintenanceUpdate() {
		LocalDate scheduleDate = this.faker.onlineGaming().scheduleDate();
		Incident incident = new Incident();
		String[] split = this.faker.onlineGaming().maintenance().split(" \\| ");
		incident.setTitle(split[0]);
		incident.setDescription(split[1]);
		incident.setStatus(IncidentStatus.SCHEDULED);
		incident.setOrigin(IncidentOrigin.MAINTENANCE);
		incident.setHappenedOn(scheduleDate);
		return incident;
	}

	public Set<InfrastructureComponent> infrastructureComponents(int count) {
		Set<InfrastructureComponent> components = new HashSet<>();
		while (components.size() < count) {
			InfrastructureComponent component = new InfrastructureComponent();
			String[] split = this.faker.onlineGaming().infrastructureComponent().split(" \\| ");
			component.setName(split[0]);
			component.setDescription(split[1]);
			component.setAvailability(Availability.OPERATIONAL);

			String availability = (components.size() != 2) ? "available" : "broken";

			String checkUrl = UriComponentsBuilder.fromPath("/{name}/{availability}")
				.build(toSlug(component.getName()), availability)
				.toString();
			component.setCheckUri(checkUrl);
			component.setCheckToken(this.faker.internet().password());

			components.add(component);
		}
		return components;
	}

	private String toSlug(String name) {
		String cleaned = name.toLowerCase().replace("\n", " ").replaceAll("[^a-z\\d\\s]", " ");
		return StringUtils.arrayToDelimitedString(StringUtils.tokenizeToStringArray(cleaned, " "), "-");
	}

}
