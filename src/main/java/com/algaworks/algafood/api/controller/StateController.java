package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.model.State;
import com.algaworks.algafood.domain.repository.StateRepository;
import com.algaworks.algafood.domain.service.RegisterStateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/states")
public class StateController {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private RegisterStateService registerState;

    @GetMapping
    public List<State> list(){
        return stateRepository.findAll();
    }

    @GetMapping("/{stateId}")
    public State search(@PathVariable Long stateId){
        return registerState.searchOrError(stateId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public State add(@RequestBody @Valid State state){
        return registerState.save(state);
    }

    @PutMapping("/{stateId}")
    public State update(@PathVariable Long stateId, @RequestBody @Valid State state){
        State stateActual = registerState.searchOrError(stateId);

        BeanUtils.copyProperties(state, stateActual, "id");

        return registerState.save(stateActual);
    }

    @DeleteMapping("/{stateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long stateId){
        registerState.delete(stateId);
    }
}