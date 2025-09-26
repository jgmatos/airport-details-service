package com.sporty.airport.http;

import com.sporty.airport.model.AirportDetails;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;


/**
 * A test case for the airport details controller
 * @param description A description of what the test is trying to achieve
 * @param apiResponse The JSON response body that should be returned by the API - Mock
 * @param expectedCode The HTTP response code that should be returned by our application
 * @param expectedBody The expected response body that should be returned by our application
 */
@Jacksonized
@Builder(toBuilder = true)
public record TestCase(
        String description,
        String url,
        String icao,
        String apiResponse,
        int expectedCode,
        AirportDetails expectedBody
) {
}