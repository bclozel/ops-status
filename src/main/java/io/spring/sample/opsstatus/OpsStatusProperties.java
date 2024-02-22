package io.spring.sample.opsstatus;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "opsstatus")
public class OpsStatusProperties {

	private final Availability availability = new Availability();

	public Availability getAvailability() {
		return this.availability;
	}

	public static class Availability {

		/**
		 * Service url for availability checks.
		 */
		private String url = "http://localhost:3030";

		public String getUrl() {
			return this.url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}

}
