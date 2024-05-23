package io.spring.sample.opsstatus.availability;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InfrastructureComponentRepository extends CrudRepository<InfrastructureComponent, Long>,
		PagingAndSortingRepository<InfrastructureComponent, Long> {

}
