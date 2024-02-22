package io.spring.sample.opsstatus.faker;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

import org.springframework.core.io.ClassPathResource;

class OnlineGaming extends AbstractProvider<BaseProviders> {

	public OnlineGaming(BaseProviders faker) {
		super(faker);
		ClassPathResource resource = new ClassPathResource("faker/online-gaming.yml");
		try {
			faker.addUrl(Locale.ENGLISH, resource.getURL());
		}
		catch (IOException ex) {
			throw new IllegalStateException("Could not load custom fake data file", ex);
		}
	}

	public String maintenance() {
		return resolve("incidents.maintenance");
	}

	public String issue() {
		return resolve("incidents.issue");
	}

	public String incidentUpdate() {
		return resolve("incidents.update");
	}

	public String infrastructureComponent() {
		return resolve("incidents.component");
	}

	public LocalDate scheduleDate() {
		return LocalDate.parse(this.faker.date().future(10, TimeUnit.DAYS, "YYYY-MM-dd"));
	}

	public LocalDate pastIncidentDate() {
		return LocalDate.parse(this.faker.date().past(30, TimeUnit.DAYS, "YYYY-MM-dd"));
	}

	public LocalDateTime reportItemTimestamp(LocalDate incidentDate) {
		return incidentDate.atStartOfDay().plus(this.faker.date().duration(24 * 60, ChronoUnit.MINUTES));
	}

}
