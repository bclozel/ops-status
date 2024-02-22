package io.spring.sample.opsstatus.availability;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;

public final class InfrastructureComponent {

	@Id
	private Long id;

	private String name;

	private String description;

	private String checkUri;

	private String checkToken;

	private LocalDateTime lastCheckedOn;

	private Availability availability;

	private List<Outage> outages = new ArrayList<>();

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCheckUri() {
		return this.checkUri;
	}

	public void setCheckUri(String checkUri) {
		this.checkUri = checkUri;
	}

	public String getCheckToken() {
		return this.checkToken;
	}

	public void setCheckToken(String checkToken) {
		this.checkToken = checkToken;
	}

	public LocalDateTime getLastCheckedOn() {
		return this.lastCheckedOn;
	}

	public void setLastCheckedOn(LocalDateTime lastCheckedOn) {
		this.lastCheckedOn = lastCheckedOn;
	}

	public Availability getAvailability() {
		return this.availability;
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public List<Outage> getOutages() {
		return this.outages;
	}

	public void setOutages(List<Outage> outages) {
		this.outages = outages;
	}

	public void addOutage(Outage outage) {
		this.outages.add(outage);
	}

	public boolean isOperational() {
		return this.availability == Availability.OPERATIONAL;
	}

	public boolean isPartiallyAvailable() {
		return this.availability == Availability.PARTIAL;
	}

	public boolean isUnavailable() {
		return this.availability == Availability.UNAVAILABLE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof InfrastructureComponent that))
			return false;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

}
