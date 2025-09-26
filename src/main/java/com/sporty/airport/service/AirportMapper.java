package com.sporty.airport.service;

import com.sporty.airport.model.AirportDetails;
import com.sporty.airport.model.ApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AirportMapper {

    @Mapping(source = "facilityName", target = "name")
    @Mapping(source = "icaoIdent", target = "icao")
    @Mapping(source = "faaIdent", target = "iata")
    AirportDetails map(ApiResponse response);
}