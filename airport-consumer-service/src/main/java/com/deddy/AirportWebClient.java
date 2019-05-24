package com.deddy;

import com.deddy.model.Airport;
import com.deddy.model.AirportDocument;
import com.deddy.repository.AirportDocumentRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

public class AirportWebClient {

    //@Autowired
    private AirportDocumentRepository airportDocumentRepository;

    private KafkaSender<Integer, AirportDocument> kafkaSender;

    private String topic;

    public AirportWebClient(AirportDocumentRepository airportDocumentRepository, KafkaSender<Integer, AirportDocument> kafkaSender, String topic) {
        this.airportDocumentRepository = airportDocumentRepository;
        this.kafkaSender = kafkaSender;
        this.topic = topic;
    }


    //private WebClient client = WebClient.create("http://localhost:3330");

    //private Flux<Airport> result = client.get()
    //        .uri("/userInfo/")
    //        .accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Airport.class);

    public void process(String serverUri, String resourceUri) {

        WebClient client = WebClient.create(serverUri);
        Flux<Airport> result = client.get()
                .uri(resourceUri)
                .retrieve().bodyToFlux(Airport.class);

        //Todo -- Add Backpressure

        result.flatMap(airport -> {
            return airportDocumentRepository.findDistinctByAirportCodeAndCityCode(airport.airportCode, airport.cityCode)
                    .switchIfEmpty(Mono.just(airport).flatMap(airport1 -> {

                        System.out.println("SwitchIfEmpty");

                        AirportDocument document = new AirportDocument();
                        document.airportCode = airport.airportCode;
                        document.airportName = airport.airportName;
                        document.cityCode = airport.cityCode;
                        document.isActive = airport.isActive;

                        return airportDocumentRepository.save(document).flatMap(temp -> {
                            return kafkaSender.<Integer>send(Flux.just(temp)
                                    .map(i -> SenderRecord.create(new ProducerRecord<>(this.topic, 1, temp), 1))).then(Mono.just(temp));
                        });
                    }))

                    .filter(airportDocument -> {
                        boolean isDifferent = false;

                        if (!airportDocument.airportName.equals(airport.airportName)) {

                            isDifferent = true;
                        }

                        if (airportDocument.isActive != airport.isActive) {
                            isDifferent = true;
                        }

                        return isDifferent;
                    }).flatMap(document -> {

                                return airportDocumentRepository.save(document);
                            }
                    ).flatMap(document -> {
                        return kafkaSender.<Integer>send(Flux.just(document)
                                .map(i -> SenderRecord.create(new ProducerRecord<>(this.topic, 1, document), 1))).then(Mono.just(document));
                    });

        }).doOnError(e -> System.out.println(e)).subscribe();

        /*

        result.flatMap(airport -> {


            return airportDocumentRepository.findDistinctByAirportCodeAndCityCode(airport.airportCode, airport.cityCode)

                    .filter(airportDocument -> {
                        boolean isDifferent = false;

                        if (!airportDocument.airportName.equals(airport.airportName)) {

                            isDifferent = true;
                        }

                        if (airportDocument.isActive != airport.isActive) {
                            isDifferent = true;
                        }

                        return isDifferent;
                    }).flatMap(document -> {
                                System.out.println(document);
                                return airportDocumentRepository.save(document);
                            }
                    ).flatMap(document -> {
                        return kafkaSender.<Integer>send(Flux.just(document)
                                .map(i -> SenderRecord.create(new ProducerRecord<>(this.topic, 1, document), 1))).then(Mono.just(document));
                    });

        }).switchIfEmpty(
                result.flatMap(airport -> {

                    System.out.println("SwitchIfEmpty");

                    AirportDocument document = new AirportDocument();
                    document.airportCode = airport.airportCode;
                    document.airportName = airport.airportName;
                    document.cityCode = airport.cityCode;
                    document.isActive = airport.isActive;

                    return airportDocumentRepository.save(document);
                }).flatMap(document -> {
                    return kafkaSender.<Integer>send(Flux.just(document)
                            .map(i -> SenderRecord.create(new ProducerRecord<>(this.topic, 1, document), 1))).then(Mono.just(document));
                })
        )

                .doOnError(e -> System.out.println(e))
                .subscribe();


         */


    }

}
