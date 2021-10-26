package com.udacity.vehicles.client.prices;

public class PriceParam {

    private Long vehicleId;

    public PriceParam() {
    }

    public PriceParam(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }
}
