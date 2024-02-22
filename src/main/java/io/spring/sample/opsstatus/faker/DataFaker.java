package io.spring.sample.opsstatus.faker;

import net.datafaker.Faker;

class DataFaker extends Faker {

	OnlineGaming onlineGaming() {
		return getProvider(OnlineGaming.class, OnlineGaming::new, this);
	}

}
