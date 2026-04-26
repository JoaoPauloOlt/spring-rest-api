package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.StateInputDisassembler;
import com.algaworks.algafood.api.assembler.StateModelAssembler;
import com.algaworks.algafood.api.model.StateModel;
import com.algaworks.algafood.api.model.input.StateInput;
import com.algaworks.algafood.domain.model.State;
import com.algaworks.algafood.domain.repository.StateRepository;
import com.algaworks.algafood.domain.service.RegisterStateService;
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

    @Autowired
    private StateModelAssembler stateModelAssembler;

    @Autowired
    private StateInputDisassembler stateInputDisassembler;

    @GetMapping
    public List<StateModel> list(){
        List<State> allStates = stateRepository.findAll();

        return stateModelAssembler.toCollectionModel(allStates);
    }

    @GetMapping("/{stateId}")
    public StateModel search(@PathVariable Long stateId){
        State state = registerState.searchOrError(stateId);

        return stateModelAssembler.toModel(state);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StateModel add(@RequestBody @Valid StateInput stateInput){
        State state = stateInputDisassembler.toDomainObject(stateInput);

        state = registerState.save(state);

        return stateModelAssembler.toModel(state);
    }

    @PutMapping("/{stateId}")
    public StateModel update(@PathVariable Long stateId, @RequestBody @Valid StateInput stateInput){
        State stateActual = registerState.searchOrError(stateId);

        stateInputDisassembler.copyToDomainObject(stateInput, stateActual);
        stateActual = registerState.save(stateActual);

        return stateModelAssembler.toModel(stateActual);
    }

    @DeleteMapping("/{stateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long stateId){
        registerState.delete(stateId);
    }
}