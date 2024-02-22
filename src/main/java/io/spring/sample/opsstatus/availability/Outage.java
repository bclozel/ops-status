package io.spring.sample.opsstatus.availability;

import java.time.LocalDateTime;

public final class Outage {

	private Availability availability;

	private LocalDateTime startedOn;

	private LocalDateTime endedOn;

	public Availability getAvailability() {
		return this.availability;
	}

	public void setAvailability(Availability availability) {
		this.availability = availability;
	}

	public LocalDateTime getStartedOn() {
		return this.startedOn;
	}

	public void setStartedOn(LocalDateTime startedOn) {
		this.startedOn = startedOn;
	}

	public LocalDateTime getEndedOn() {
		return this.endedOn;
	}

	public void setEndedOn(LocalDateTime endedOn) {
		this.endedOn = endedOn;
	}

}
