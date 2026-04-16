package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Restaurant;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantRepositoryQueries {
    List<Restaurant> find(String name, BigDecimal feeInitial, BigDecimal feeFinal);

    List<Restaurant> findWithFeeFree(String name);
}
