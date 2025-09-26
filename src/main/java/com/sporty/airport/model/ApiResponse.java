package com.sporty.airport.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The response from the airport details API
 */
@Data
public class ApiResponse {

    @JsonProperty("site_number")
    private String siteNumber;

    private String type;

    @JsonProperty("facility_name")
    private String facilityName;

    @JsonProperty("faa_ident")
    private String faaIdent;

    @JsonProperty("icao_ident")
    private String icaoIdent;

    private String region;

    @JsonProperty("district_office")
    private String districtOffice;

    private String state;

    @JsonProperty("state_full")
    private String stateFull;

    private String county;
    private String city;
    private String ownership;
    private String use;
    private String manager;

    @JsonProperty("manager_phone")
    private String managerPhone;

    private String latitude;

    @JsonProperty("latitude_sec")
    private String latitudeSec;

    private String longitude;

    @JsonProperty("longitude_sec")
    private String longitudeSec;

    private String elevation;

    @JsonProperty("magnetic_variation")
    private String magneticVariation;

    private String tpa;

    @JsonProperty("vfr_sectional")
    private String vfrSectional;

    @JsonProperty("boundary_artcc")
    private String boundaryArtcc;

    @JsonProperty("boundary_artcc_name")
    private String boundaryArtccName;

    @JsonProperty("responsible_artcc")
    private String responsibleArtcc;

    @JsonProperty("responsible_artcc_name")
    private String responsibleArtccName;

    @JsonProperty("fss_phone_number")
    private String fssPhoneNumber;

    @JsonProperty("fss_phone_numer_tollfree")
    private String fssPhoneNumberTollfree;

    @JsonProperty("notam_facility_ident")
    private String notamFacilityIdent;

    private String status;

    @JsonProperty("certification_typedate")
    private String certificationTypedate;

    @JsonProperty("customs_airport_of_entry")
    private String customsAirportOfEntry;

    @JsonProperty("military_joint_use")
    private String militaryJointUse;

    @JsonProperty("military_landing")
    private String militaryLanding;

    @JsonProperty("lighting_schedule")
    private String lightingSchedule;

    @JsonProperty("beacon_schedule")
    private String beaconSchedule;

    @JsonProperty("control_tower")
    private String controlTower;

    private String unicom;
    private String ctaf;

    @JsonProperty("effective_date")
    private String effectiveDate;
}