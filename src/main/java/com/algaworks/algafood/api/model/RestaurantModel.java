package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Setter
@Getter
public class RestaurantModel {

    private Long id;

    private String name;

    private BigDecimal shippingFee;

    private KitchenModel kitchen;

    private OffsetDateTime dateRegister;

    private OffsetDateTime dateUpdate;
}
