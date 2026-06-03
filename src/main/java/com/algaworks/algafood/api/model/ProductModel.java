package com.algaworks.algafood.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductModel {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;
}
