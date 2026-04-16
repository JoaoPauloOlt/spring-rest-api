package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends CustomJpaRepository<Restaurant, Long>, RestaurantRepositoryQueries, JpaSpecificationExecutor<Restaurant> {

    @Query("from Restaurant r join fetch r.kitchen")
    List<Restaurant> findAll();

    List<Restaurant> findByShippingFeeBetween(BigDecimal FeeInitial, BigDecimal FeeFinal);

    List<Restaurant> findByNameContainingAndKitchenId(String name, Long kitchenId);

    Optional<Restaurant> findFirstRestaurantByNameContaining(String name);

}
