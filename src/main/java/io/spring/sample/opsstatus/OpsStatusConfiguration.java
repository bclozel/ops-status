package io.spring.sample.opsstatus;

import io.spring.sample.opsstatus.checker.AvailabilityCheckClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OpsStatusProperties.class)
@EnableScheduling
class OpsStatusConfiguration {

	@Bean
	AvailabilityCheckClient availabilityCheck(RestClient.Builder builder, OpsStatusProperties properties) {
		return new AvailabilityCheckClient(builder, properties.getAvailability().getUrl());
	}

}
