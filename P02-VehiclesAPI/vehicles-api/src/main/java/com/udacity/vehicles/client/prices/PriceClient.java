package com.udacity.vehicles.client.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Implements a class to interface with the Pricing Client for price data.
 */
@Service
public class PriceClient {

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

    private final PriceFeignClient priceFeignClient;

    public PriceClient(PriceFeignClient priceFeignClient) {
        this.priceFeignClient = priceFeignClient;
    }

    /**
     * Gets a vehicle price from the pricing client, given vehicle ID.
     *
     * @param vehicleId ID number of the vehicle for which to get the price
     * @return Currency and price of the requested vehicle,
     * error message that the vehicle ID is invalid, or note that the
     * service is down.
     */
    public String getPrice(Long vehicleId) {
        try {
            PriceParam priceParam = new PriceParam(vehicleId);
            Price price = priceFeignClient.getPriceById(priceParam);
            return String.format("%s %s", price.getCurrency(), price.getPrice());
        } catch (Exception e) {
            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
        }
        return "(consult price)";
    }

    @FeignClient("pricing-service")
    static interface PriceFeignClient {
        @GetMapping(value = "/api/prices/search/findByVehicleId")
        public Price getPriceById(@SpringQueryMap PriceParam param);
    }

}
