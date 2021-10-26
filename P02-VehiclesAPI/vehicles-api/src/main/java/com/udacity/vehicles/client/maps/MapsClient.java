package com.udacity.vehicles.client.maps;

import com.udacity.vehicles.domain.Location;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

/**
 * Implements a class to interface with the Maps Client for location data.
 */
@Service
public class MapsClient {

    private static final Logger log = LoggerFactory.getLogger(MapsClient.class);

    private MapsFeignClient mapsFeignClient;

    private final ModelMapper mapper;

    public MapsClient(MapsFeignClient mapsFeignClient,
                      ModelMapper mapper) {
        this.mapsFeignClient = mapsFeignClient;
        this.mapper = mapper;
    }

    /**
     * Gets an address from the Maps client, given latitude and longitude.
     *
     * @param location An object containing "lat" and "lon" of location
     * @return An updated location including street, city, state and zip,
     * or an exception message noting the Maps service is down
     */
    public Location getAddress(Location location) {
        try {
            MapsParam mapsParam = new MapsParam(location.getLat(), location.getLon());
            Address address = mapsFeignClient.getAddress(mapsParam);
            mapper.map(Objects.requireNonNull(address), location);
            return location;
        } catch (Exception e) {
            log.warn("Map service is down");
            return location;
        }
    }

    @FeignClient("map-service")
    static interface MapsFeignClient {
        @GetMapping(value = "/maps")
        public Address getAddress(@SpringQueryMap MapsParam param);

    }

}
