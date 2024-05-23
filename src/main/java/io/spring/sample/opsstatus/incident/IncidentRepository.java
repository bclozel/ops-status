package io.spring.sample.opsstatus.incident;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IncidentRepository extends CrudRepository<Incident, Long>, PagingAndSortingRepository<Incident, Long> {

	List<Incident> findAllByStatusOrderByHappenedOnDesc(IncidentStatus status);

	Slice<Incident> findByStatusOrderByHappenedOnDesc(IncidentStatus status, Pageable pageable);

	@Query("SELECT * FROM incident ORDER BY happened_on DESC")
	Stream<Incident> streamAll();

}
