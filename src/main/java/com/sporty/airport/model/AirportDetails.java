package com.sporty.airport.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Our own representation of the airport details
 *
 * @param name The facility name
 * @param type The facility type, e.g. "airport"
 * @param icao International Civil Aviation Organization code
 * @param iata International Air Transport Association code
 * @param city The city where the facility is located
 */
@Jacksonized
@Builder(toBuilder = true)
public record AirportDetails(
        String name,
        String type,
        String icao,
        String iata,
        String city
) {

    public static AirportDetails unavailable(String icao) {
        return new AirportDetails(
                "Unavailable",
                null,
                icao,
                null,
                null
        );
    }

}