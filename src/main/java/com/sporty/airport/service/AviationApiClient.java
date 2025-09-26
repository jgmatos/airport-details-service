package com.sporty.airport.service;

import com.sporty.airport.model.AirportDetails;
import com.sporty.airport.model.ApiResponse;
import com.sporty.airport.properties.AviationApiProperties;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AviationApiClient implements AirportClient {

    private static final String LOG_SYMBOL = "✈️";

    /**
     * Config properties
     */
    private final AviationApiProperties aviationApiProperties;

    /**
     * Map between the API response and our own representation of the airport details
     */
    private final AirportMapper airportMapper;

    /**
     * Circuit breaker factory
     */
    private final ReactiveResilience4JCircuitBreakerFactory cbFactory;

    /**
     * Metrics
     */
    private final MeterRegistry meterRegistry;

    /**
     * To perform http requests to the aviationapi.com API
     */
    private WebClient webClient;

    @PostConstruct
    void initWebClient() {
        this.webClient = WebClient.builder()
                .baseUrl(aviationApiProperties.getBaseUrl())
                .build();
    }



    private Counter retryCounter;
    private Counter fallbackCounter;

    @PostConstruct
    void initMetrics() {

        this.retryCounter = Counter.builder("airport_retry_count")
                .description("Number of retries")
                .register(meterRegistry);

        this.fallbackCounter = Counter.builder("airport_api_fallbacks")
                .description("Number of fallback calls")
                .register(meterRegistry);
    }


    @Override
    public Mono<AirportDetails> fetchAirport(String icao) {
        return cbFactory.create("airportService").run(
                fetchFromApi(icao)
                        .map(airportMapper::map)
                        .timeout(Duration.ofSeconds(aviationApiProperties.getTimeoutSeconds()))
                        .retryWhen(buildRetry())
                        .onErrorResume(e -> handleFallback(icao, e))
        );
    }

    /**
     * Fetches the airport details from the aviationapi.com API.
     */
    private Mono<ApiResponse> fetchFromApi(String icao) {
        return webClient.get()
                .uri("/airports?apt={icao}", icao)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, List<ApiResponse>>>() {
                })
                .map(map -> map.get(icao).getFirst())
                .doOnNext(resp -> log.info("{} Raw API response: {}", LOG_SYMBOL, resp));
    }

    /**
     * Retry strategy
     */
    private Retry buildRetry() {
        return Retry.backoff(
                        aviationApiProperties.getRetries(),
                        Duration.ofMillis(aviationApiProperties.getBackoffMillis())
                )
                .doAfterRetry(retry -> retryCounter.increment())
                .maxBackoff(Duration.ofSeconds(aviationApiProperties.getMaxBackoffMillis()))
                .onRetryExhaustedThrow((spec, signal) -> signal.failure());
    }

    /**
     * Fallback
     */
    private Mono<AirportDetails> handleFallback(String icao, Throwable e) {
        log.error("{} Error fetching airport info for {}", LOG_SYMBOL, icao, e);
        fallbackCounter.increment();
        return Mono.just(AirportDetails.unavailable(icao));
    }
}