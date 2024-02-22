package io.spring.sample.opsstatus.incident;

import java.time.LocalDateTime;

public final class ReportItem {

	private ItemType type;

	private LocalDateTime date;

	private String description;

	public static ReportItem create(ItemType type, String description) {
		ReportItem investigation = new ReportItem();
		investigation.type = type;
		investigation.description = description;
		investigation.date = LocalDateTime.now();
		return investigation;
	}

	public ItemType getType() {
		return this.type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public LocalDateTime getDate() {
		return this.date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
