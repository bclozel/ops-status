package io.spring.sample.opsstatus.incident;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public final class Incident {

	@Id
	private Long id;

	private String title;

	private String description;

	private LocalDate happenedOn;

	private IncidentOrigin origin;

	private IncidentStatus status;

	private List<ReportItem> reportItems = new ArrayList<>();

	public static Incident declareIncident(String title, String description) {
		Incident incident = new Incident();
		incident.title = title;
		incident.description = description;
		incident.happenedOn = LocalDate.now();
		incident.origin = IncidentOrigin.ISSUE;
		incident.status = IncidentStatus.IN_PROGRESS;
		return incident;
	}

	public static Incident scheduleMaintenance(String title, String description, LocalDate maintenanceDate) {
		Incident incident = new Incident();
		incident.title = title;
		incident.happenedOn = LocalDate.now();
		incident.origin = IncidentOrigin.MAINTENANCE;
		incident.status = IncidentStatus.SCHEDULED;
		return incident;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getHappenedOn() {
		return this.happenedOn;
	}

	public void setHappenedOn(LocalDate happenedOn) {
		this.happenedOn = happenedOn;
	}

	public IncidentOrigin getOrigin() {
		return this.origin;
	}

	public void setOrigin(IncidentOrigin origin) {
		this.origin = origin;
	}

	public IncidentStatus getStatus() {
		return this.status;
	}

	public void setStatus(IncidentStatus status) {
		this.status = status;
	}

	public List<ReportItem> getReportItems() {
		return this.reportItems;
	}

	public void setReportItems(List<ReportItem> reportItems) {
		this.reportItems = reportItems;
	}

	public void addReportItem(ReportItem reportItem) {
		this.reportItems.add(reportItem);
	}

	public void resolve() {
		this.status = IncidentStatus.RESOLVED;
	}

	public boolean isMaintenance() {
		return this.origin == IncidentOrigin.MAINTENANCE;
	}

	public boolean isIssue() {
		return this.origin == IncidentOrigin.ISSUE;
	}

}
