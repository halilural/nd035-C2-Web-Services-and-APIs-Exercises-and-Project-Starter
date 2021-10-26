package com.udacity.pricing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.pricing.domain.price.Price;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PricingIntegrationTest {

    @LocalServerPort
    private Integer port;

    private String PATH = "http://localhost:port/api/prices";

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * INSERT INTO price (id, currency, price, vehicle_id)
     * VALUES (1, 'YTL', 15.00, 3);
     * INSERT INTO price (id, currency, price, vehicle_id)
     * VALUES (2, 'YTL', 30.00, 2);
     */

    private static final Long marketPriceId = 1L;
    private static final Long stockPriceId = 2L;
    private static final Long cryptoPriceId = 3L;

    private static final Long dummyVehicleId = 1L;

    private static final Price cryptoPrice = new Price(cryptoPriceId, "EUR", new BigDecimal(265.00), 3L);

    @BeforeAll
    public static void setup() {

    }

    @BeforeEach
    public void beforeEach() {
        PATH = PATH.replace("port", port.toString());
    }

    @Test
    @Order(1)
    public void testGetAll() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode node = mapper.readTree(restTemplate.getForObject(PATH, String.class))
                .findPath("prices");
        List<Price> price = mapper.readValue(node.toString(), new TypeReference<List<Price>>() {
        });
        Assertions.assertEquals(2, price.size());
    }

    @Test
    @Order(2)
    public void testGetById() {
        Price marketPriceResponse = getPrice(marketPriceId);
        Assertions.assertNotNull(marketPriceResponse);
    }

    @Test
    @Order(2)
    public void testGetByVehicleId() {
        Price price = getPriceWithVehicleId(dummyVehicleId);
        Assertions.assertNotNull(price);
    }

    @Test
    @Order(3)
    public void testCreate() {
        HttpEntity<Price> request = new HttpEntity<>(cryptoPrice);
        Price cryptoPrice = restTemplate.postForObject(PATH, request, Price.class);
        System.out.println("Id of the Crypto Price: " + cryptoPrice.getId());
        Assertions.assertNotNull(cryptoPrice);
    }


    @Test
    @Order(4)
    public void testUpdate() {
        Price price = getPrice(marketPriceId);
        price.setCurrency("USD");
        HttpEntity<Price> request = new HttpEntity<>(price);
        ResponseEntity<Price> response = restTemplate.exchange(PATH + "/" + marketPriceId, HttpMethod.PUT, request, Price.class);
        Price responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("USD", responseBody.getCurrency());
    }


    @Test
    @Order(5)
    public void testDelete() {
        restTemplate.delete(PATH + "/" + marketPriceId);
        Assertions.assertNull(getPrice(marketPriceId));
    }

    private Price getPrice(Long id) {
        return restTemplate.getForObject(PATH + "/" + id, Price.class);
    }

    private Price getPriceWithVehicleId(Long vehicleId) {
        return restTemplate.getForObject(PATH + "/search/findByVehicleId?vehicleId=" + vehicleId, Price.class);
    }

}