package com.deddy;

import com.deddy.model.AirportDocument;
import com.deddy.repository.AirportDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;

import java.time.Duration;

@SpringBootApplication
public class Application implements CommandLineRunner {


    @Autowired
    private AirportDocumentRepository airportDocumentRepository;

    @Autowired
    private KafkaSender<Integer, AirportDocument> kafkaSender;

    @Value("${kafka.topic}")
    private String topic;

    @Value("${airport.serverUri}")
    private String serverUri;

    @Value("${airport.resourceUri}")
    private String resourceUri;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        //airportDocumentRepository.saveAll(Flux.just(johnAoe, johnBoe, johnCoe, johnDoe)).subscr


    }

    @Override
    public void run(String args[]) {

        //airportDocumentRepository.save(new AirportDocument()).block();

        Flux.interval(Duration.ofSeconds(0), Duration.ofSeconds(300)).subscribe( time -> {

                    //System.out.println("Calling server");
                    AirportWebClient gwc = new AirportWebClient(airportDocumentRepository, kafkaSender, this.topic);
                    gwc.process(serverUri, resourceUri);
                }
        );
        //System.out.println(gwc.getResult());
    }
}
