package com.sporty.airport.service;

import com.sporty.airport.model.AirportDetails;
import reactor.core.publisher.Mono;

public interface AirportClient {
    Mono<AirportDetails> fetchAirport(String icao);
}