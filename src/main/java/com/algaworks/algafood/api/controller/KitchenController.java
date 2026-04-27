package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.KitchenInputDisassembler;
import com.algaworks.algafood.api.assembler.KitchenModelAssembler;
import com.algaworks.algafood.api.model.KitchenModel;
import com.algaworks.algafood.api.model.input.KitchenInput;
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

    @Autowired
    private KitchenModelAssembler kitchenModelAssembler;

    @Autowired
    private KitchenInputDisassembler kitchenInputDisassembler;

    @GetMapping
    public List<KitchenModel> list(){
        List<Kitchen> allKitchens = kitchenRepository.findAll();

        return kitchenModelAssembler.toCollectionModel(allKitchens);
    }

    @GetMapping("/{kitchenId}")
    public KitchenModel search(@PathVariable Long kitchenId){
        Kitchen kitchen = registerKitchen.searchOrError(kitchenId);

        return kitchenModelAssembler.toModel(kitchen);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KitchenModel add(@RequestBody @Valid KitchenInput kitchenInput){
        Kitchen kitchen = kitchenInputDisassembler.toDomainObject(kitchenInput);
        kitchen = registerKitchen.save(kitchen);

       return kitchenModelAssembler.toModel(kitchen);
    }

    @PutMapping("/{kitchenId}")
    public KitchenModel update(@PathVariable Long kitchenId, @RequestBody @Valid KitchenInput kitchenInput){
        Kitchen kitchenActual = registerKitchen.searchOrError(kitchenId);
        kitchenInputDisassembler.copyToDomainObject(kitchenInput, kitchenActual);
        kitchenActual = registerKitchen.save(kitchenActual);

        return kitchenModelAssembler.toModel(kitchenActual);
    }

    @DeleteMapping("/{kitchenId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long kitchenId){
        registerKitchen.delete(kitchenId);
    }
}