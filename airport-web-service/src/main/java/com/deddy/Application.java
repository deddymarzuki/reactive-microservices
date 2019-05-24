package com.deddy;

import com.deddy.model.AirportDocument;
import com.deddy.repository.AirportDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.kafka.receiver.KafkaReceiver;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    KafkaReceiver<Integer, AirportDocument> kafkaReceiver;

    @Autowired
    private AirportDocumentRepository airportDocumentRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //Connect to Kafka to receive stream of datas
        kafkaReceiver
                .receive()
                .flatMap(record -> airportDocumentRepository.save(record.value()))
                .doOnError(e -> System.out.println(e))
                .subscribe();



    }
}
