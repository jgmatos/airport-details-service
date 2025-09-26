# Airport Details Service

A Spring Boot reactive microservice that fetches airport details via an external API (aviationapi.com).

## Setup & Run

### Prerequisites
- Java 21
- Gradle 8+


### Build
`./gradlew clean build`

### Run
`./gradlew bootRun`

The service will start on http://localhost:7007.


Example Request

`curl http://localhost:7007/airport/KJFK`

Response:

```
{
"name": "John F Kennedy Intl",
"type": "AIRPORT",
"icao": "KJFK",
"iata": "JFK",
"city": "New York"
}
```



### Test

`./gradlew test`

Added a test class AirportDetailsTest that uses parameterized tests. Each test case is defined by a JSON file containing 
the mock API response and the expected result. This approach allows adding new test scenarios simply by creating new JSON 
files without writing additional test code.




## Design and Decisions

### Scalability
- HTTP requests to the external airport API are non-blocking using Reactor, preventing thread starvation and improving throughput under load.
- Caching by ICAO ensures that repeated requests for the same airport do not trigger redundant API calls, reducing latency and load on the external service.

### Resilience
- Retry logic with configurable attempts and exponential backoff ensures temporary failures are automatically retried.
- Fallback strategy returns `AirportDetails.unavailable()` if all retries fail.
- Circuit breaker via `ReactiveResilience4JCircuitBreakerFactory` protects the system from cascading failures when the external API is unstable.

### Extensibility
- Clear **layering** separates responsibilities:
    - `AirportDetailsService` orchestrates domain logic.
    - `AviationApiClient` encapsulates external API calls.
    - `AirportMapper` transforms API responses into the domain model.
- Adding new external providers or changing response formats can be done with minimal impact to existing code.

### Observability
- Logging captures raw API responses and errors for debugging.
- Metrics record request durations, retry counts, and other key indicators.
- Global exception handling provides consistent error responses.
  -	Validation: Invalid ICAO codes trigger InvalidIcaoException → 400 Bad Request
  -	API Errors: Network or external API failures trigger fallback → returns a default AirportDetails object
  -	Unexpected Errors: Caught globally and mapped to 500 Internal Server Error



### Assumptions
- Only 4-letter ICAO codes are supported. Inputs must be uppercase letters (A-Z).
- The AirportDetails response model includes only the five fields considered relevant, ignoring other data returned by the external API.
- External airport API (aviationapi.com) is assumed to return JSON responses with the expected structure.
- If the external API fails after retries, the system returns a placeholder `AirportDetails.unavailable()`.
- Caching is based on ICAO codes only; updates to airport data externally are not reflected until cache expiration.
- Metrics are collected in-memory via Micrometer; a Prometheus/Grafana setup is assumed for full observability in production.

## AI usage
- The ApiResponse POJO was generated using AI.
- Minor AI-assisted refactoring and polishing of code blocks for readability and style, as well as drafting this documentation.
- Guidance on resilience techniques, particularly ReactiveResilience4JCircuitBreakerFactory, as I’m less experienced with them
