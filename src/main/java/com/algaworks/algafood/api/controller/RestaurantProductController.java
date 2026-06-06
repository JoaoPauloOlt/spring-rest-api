package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.ProductInputDisassembler;
import com.algaworks.algafood.api.assembler.ProductModelAssembler;
import com.algaworks.algafood.api.model.ProductModel;
import com.algaworks.algafood.api.model.input.ProductInput;
import com.algaworks.algafood.domain.model.Product;
import com.algaworks.algafood.domain.model.Restaurant;
import com.algaworks.algafood.domain.repository.ProductRepository;
import com.algaworks.algafood.domain.service.RegisterProductService;
import com.algaworks.algafood.domain.service.RegisterRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/products")
public class RestaurantProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RegisterProductService registerProduct;

    @Autowired
    private RegisterRestaurantService registerRestaurant;

    @Autowired
    private ProductModelAssembler productModelAssembler;

    @Autowired
    private ProductInputDisassembler productInputDisassembler;

    @GetMapping
    public List<ProductModel> list(@PathVariable Long restaurantId){
        Restaurant restaurant = registerRestaurant.searchOrError(restaurantId);

        List<Product> allProducts = productRepository.findByRestaurant(restaurant);

        return productModelAssembler.toCollectionModel(allProducts);
    }

    @GetMapping("/{productId}")
    public ProductModel search(@PathVariable Long restaurantId, @PathVariable Long productId){
        Product product = registerProduct.searchOrFail(restaurantId, productId);

        return productModelAssembler.toModel(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductModel create(@PathVariable Long restaurantId, @RequestBody @Valid ProductInput productInput){
        Restaurant restaurant = registerRestaurant.searchOrError(restaurantId);

        Product product = productInputDisassembler.toDomainObject(productInput);
        product .setRestaurant(restaurant);

        product = registerProduct.create(product);

        return productModelAssembler.toModel(product);
    }

    @PutMapping("/{productId}")
    public ProductModel update(@PathVariable Long restaurantId, @PathVariable Long productId,
                               @RequestBody @Valid ProductInput productInput){
        Product productActual = registerProduct.searchOrFail(restaurantId, productId);

        productInputDisassembler.copyToDomainObject(productInput, productActual);

        productActual = registerProduct.create(productActual);

        return productModelAssembler.toModel(productActual);
    }
}
