package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Kitchen;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KitchenRepository extends CustomJpaRepository<Kitchen, Long> {

    List<Kitchen> findAllByNameContaining(String name);

    Optional<Kitchen> findByName(String name);
}
