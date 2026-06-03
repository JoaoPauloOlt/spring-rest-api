package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.ProductNotFoundException;
import com.algaworks.algafood.domain.model.Product;
import com.algaworks.algafood.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product create(Product product){
        return productRepository.save(product);
    }

    public Product searchOrFail(Long restaurantId, Long productId){
        return productRepository.findById(restaurantId, productId)
                .orElseThrow(() -> new ProductNotFoundException(restaurantId, productId));
    }
}
