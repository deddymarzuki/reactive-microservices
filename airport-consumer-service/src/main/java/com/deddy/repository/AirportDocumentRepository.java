package com.deddy.repository;

import com.deddy.model.AirportDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AirportDocumentRepository extends ReactiveCrudRepository<AirportDocument, Id> {

    @Query("{ airportCode : ?0, cityCode: ?1}")
    Mono<AirportDocument> findDistinctByAirportCodeAndCityCode(String airportCode, String city);

}
