package com.sporty.airport.service;

import com.sporty.airport.exception.InvalidIcaoException;
import com.sporty.airport.model.AirportDetails;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirportDetailsService {

    /**
     * For log reading convenience
     */
    private static final String LOG_SYMBOL = "🗼";

    /**
     * ICAO code pattern validation.
     * - Must be 4 characters long
     * - Must be uppercase letters only
     */
    private static final Pattern ICAO_PATTERN = Pattern.compile("^[A-Z]{4}$");
    public static final int NANO_TO_MILLIS = 1000000;

    /**
     * Airport client
     */
    private final AirportClient airportClient;

    /**
     * Metrics
     */
    private final MeterRegistry meterRegistry;

    /**
     * The time elapsed for retrieving airport details from the airport client API
     */
    private Timer timeDistribution;

    /**
     * Count how many times the service method actually runs
     */
    private Counter cacheMissCounter;


    @PostConstruct
    void initMetrics() {
        timeDistribution = Timer
                .builder("airport.client.retrieval.time")
                .publishPercentileHistogram()
                .register(meterRegistry);

        cacheMissCounter = Counter.builder("airport.cache.misses")
                .description("Number of cache misses in airport retrieval")
                .register(meterRegistry);
    }

    /**
     * Fetches the airport details for the given ICAO code.
     * - Use Mono for asynchronous processing
     * - Use cache to avoid unnecessary requests to the aviationapi.com API
     *
     * @param icao The ICAO code of the airport to fetch details for
     * @return The airport details for the given ICAO code
     */
    @Cacheable(value = "airports", key = "#icao")
    public Mono<AirportDetails> fetchDetails(String icao) {

        log.info("{} Fetching airport info for ICAO: {}", LOG_SYMBOL, icao);

        if (icao == null || icao.isBlank() || !ICAO_PATTERN.matcher(icao).matches())
            throw new InvalidIcaoException("Invalid ICAO code: " + icao);

        cacheMissCounter.increment();

        long start = System.nanoTime();
        var result = airportClient.fetchAirport(icao);
        long duration = System.nanoTime() - start / NANO_TO_MILLIS;

        log.info("{} Fetched airport info for ICAO: {} in {}ms", LOG_SYMBOL, icao, duration);
        timeDistribution.record(duration, TimeUnit.MILLISECONDS);

        return result;
    }

}
