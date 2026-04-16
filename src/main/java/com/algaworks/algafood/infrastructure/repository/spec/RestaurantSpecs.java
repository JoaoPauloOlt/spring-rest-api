package com.algaworks.algafood.infrastructure.repository.spec;

import com.algaworks.algafood.domain.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestaurantSpecs {

    public static Specification<Restaurant> withFeeFree() {
        return ((root, query, builder) ->
                builder.equal(root.get("shippingFee"), BigDecimal.ZERO));
    }

    public static Specification<Restaurant> withNameSimilar(String name) {
        return ((root, query, builder) ->
                builder.like(root.get("name"), "%" + name + "%"));
    }
}
