package com.deddy.repository;

import com.deddy.model.AirportDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface AirportDocumentRepository extends ReactiveCrudRepository<AirportDocument, Id> {

    @Query("{ id: { $exists: true }}")
    Flux<AirportDocument> findAllPageable(final Pageable page);

    @Query("{ isActive: ?0}")
    Flux<AirportDocument> findAirportDocumentsByStatus(boolean isActive, final Pageable page);
}
