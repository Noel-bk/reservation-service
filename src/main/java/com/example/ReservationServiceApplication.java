package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.stream.Stream;

@SpringBootApplication
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
}

@RestController
@RefreshScope
class MessageRestController {
    private final String value;


    @Autowired
    public MessageRestController(@Value("${message}") String value) {
        this.value = value;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/message")
    public String read() {
        return this.value;
    }
}


@Component
class SampleDataCLR implements CommandLineRunner {

    private final ReservationRepository reservationRepository;

    @Autowired
    public SampleDataCLR(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Stream.of("Penny", "Bernadette", "Amy", "Emily")
            .forEach(name -> reservationRepository.save(new Reservation(name)));
        reservationRepository.findAll().forEach(System.out::println);
    }
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

}


@Entity
class Reservation {

    public Reservation() {
    }

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String reservationName; // reservation_name

    public Long getId() {
        return id;
    }

    public String getReservationName() {
        return reservationName;
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + id +
            ", reservationName='" + reservationName + '\'' +
            '}';
    }
}
