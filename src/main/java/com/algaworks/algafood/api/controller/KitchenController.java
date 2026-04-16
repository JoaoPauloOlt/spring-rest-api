package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.Kitchen;
import com.algaworks.algafood.domain.repository.KitchenRepository;
import com.algaworks.algafood.domain.service.RegisterKitchenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/kitchens")
public class KitchenController {

    @Autowired
    private KitchenRepository kitchenRepository;

    @Autowired
    private RegisterKitchenService registerKitchen;

    @GetMapping
    public List<Kitchen> list(){
        return kitchenRepository.findAll();
    }

    @GetMapping("/{kitchenId}")
    public Kitchen search(@PathVariable Long kitchenId){
        return registerKitchen.searchOrError(kitchenId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Kitchen add(@RequestBody @Valid Kitchen kitchen){
       return registerKitchen.save(kitchen);
    }

    @PutMapping("/{kitchenId}")
    public Kitchen update(@PathVariable Long kitchenId, @RequestBody @Valid Kitchen kitchen){
        Kitchen kitchenActual = registerKitchen.searchOrError(kitchenId);

        BeanUtils.copyProperties(kitchen, kitchenActual, "id");

        return registerKitchen.save(kitchenActual);
    }

    @DeleteMapping("/{kitchenId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long kitchenId){
        registerKitchen.delete(kitchenId);
    }
}