package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.input.GrupoInput;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.RegisterGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private RegisterGrupoService registerGrupo;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private GrupoInputDisassembler grupoInputDisassembler;

    @GetMapping
    public List<GrupoModel> list(){
        List<Grupo> allGrupos = grupoRepository.findAll();

        return grupoModelAssembler.toCollectionModel(allGrupos);
    }

    @GetMapping("/{grupoId}")
    public GrupoModel search(@PathVariable Long grupoId){
        Grupo grupo = registerGrupo.searchOrFail(grupoId);

        return grupoModelAssembler.toModel(grupo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel create(@RequestBody @Valid GrupoInput grupoInput){
        Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);

        grupo = registerGrupo.save(grupo);

        return grupoModelAssembler.toModel(grupo);
    }

    public GrupoModel update(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput){
        Grupo grupoActual = registerGrupo.searchOrFail(grupoId);

        grupoInputDisassembler.copyToDomainObject(grupoInput, grupoActual);

        grupoActual = registerGrupo.save(grupoActual);

        return grupoModelAssembler.toModel(grupoActual);
    }

    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long grupoId){
        registerGrupo.delete(grupoId);
    }
}
