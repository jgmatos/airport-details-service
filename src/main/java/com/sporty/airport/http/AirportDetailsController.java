package com.sporty.airport.http;


import com.sporty.airport.model.AirportDetails;
import com.sporty.airport.service.AirportDetailsService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/airport")
@RequiredArgsConstructor
public class AirportDetailsController {

    private final AirportDetailsService airportDetailsService;


    @GetMapping("/{icao}")
    public Mono<AirportDetails> fetchDetails(

            @Parameter(description = "The ICAO Airport Code")
            @PathVariable
            String icao

    ) {

        return airportDetailsService.fetchDetails(icao);

    }




}
