package com.deddy.controller;

import com.deddy.model.AirportDocument;
import com.deddy.repository.AirportDocumentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/airport")
public class AirportReactiveController {

    private final AirportDocumentRepository airportDocumentRepository;

    public AirportReactiveController(AirportDocumentRepository airportDocumentRepository) {
        this.airportDocumentRepository = airportDocumentRepository;
    }

    @GetMapping
    private Flux<AirportDocument> getAirports(@RequestParam(defaultValue = "10") int start,
                                              @RequestParam(defaultValue = "10") int limit,
                                              @RequestParam(defaultValue = "") String status) {

        //TODO: Caching

        if (!status.isEmpty()) {

            String lowerCase = status.toLowerCase();

            boolean isActive = true;

            switch (lowerCase) {

                case "inactive":
                    isActive = false;
                    break;

                case "active":
                    isActive = true;
                    break;
            }

            if (lowerCase.equals("inactive")) {
                isActive = false;
            }

            return airportDocumentRepository.findAirportDocumentsByStatus(isActive, PageRequest.of(start, limit));
        }

        return airportDocumentRepository.findAllPageable(PageRequest.of(start, limit));
    }
}
