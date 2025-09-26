package com.sporty.airport.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.airport.service.AirportDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureMockMvc
public class AirportDetailsTest {

    public static final String DIR_TESTCASES = "/test-cases";


    @MockitoSpyBean
    private AirportDetailsService airportDetailsService;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RestTemplate restTemplate;


    private MockRestServiceServer mockServer;


    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }


    @MethodSource("testCases")
    @ParameterizedTest(name = "#{index} {0}")
    public void fetchDetailsTest(File file) throws Exception {

        TestCase testCase = jsonObjectMapper.readValue(file, TestCase.class);

        //mock responses of http requests to aviation api
        mockServer.expect(requestTo(testCase.url()))
                .andRespond(withSuccess(testCase.apiResponse(), MediaType.APPLICATION_JSON));

        int expectedCode = testCase.expectedCode();
        String expectedBody = jsonObjectMapper.writeValueAsString(testCase.expectedBody());

        if (expectedCode >= 200 && expectedCode < 300) { //in cases where we expect a successful response
            //request and assert the response is as expected
            requestAndAssertBody(testCase.icao(), expectedCode, expectedBody);

            //perform the request a second time to validate caching
            requestAndAssertBody(testCase.icao(), expectedCode, expectedBody);
            Mockito.verify(airportDetailsService, Mockito.times(1)).fetchDetails(testCase.icao());

        } else { //cases where we expect an unsuccessful response
           requestAndAssertCode(testCase.icao(), expectedCode);
        }

    }


    private WebTestClient.BodyContentSpec requestAndAssertCode(String icao, int expectedCode) {
        return webTestClient.get()
                .uri("/airport/{icao}", icao)
                .exchange()
                .expectStatus().isEqualTo(expectedCode)
                .expectBody(); // return the body spec so you can chain more expectations
    }

    private void requestAndAssertBody(String icao, int expectedCode, String expectedBody) {
        requestAndAssertCode(icao, expectedCode)
                .json(expectedBody); // order-insensitive, just like MockMvc
    }

    private static Stream<Arguments> testCases() throws IOException, URISyntaxException {
        Path testDir = Paths.get(
                Objects.requireNonNull(AirportDetailsTest.class.getResource(DIR_TESTCASES)).toURI()
        );

        List<File> files;
        try (Stream<Path> paths = Files.walk(testDir)) {
            files = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .map(Path::toFile)
                    .toList();
        }

        return files.stream()
                .map(file -> Arguments.of(Named.of(file.getName(), file)));
    }


}