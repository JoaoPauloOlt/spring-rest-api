package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.PermissionModelAssembler;
import com.algaworks.algafood.api.model.PermissionModel;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.service.RegisterGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/grupos/{grupoId}/permissions")
public class GrupoPermissionController {

    @Autowired
    private RegisterGrupoService registerGrupo;

    @Autowired
    private PermissionModelAssembler permissionModelAssembler;

    @GetMapping
    public List<PermissionModel> list(@PathVariable Long grupoId){
        Grupo grupo = registerGrupo.searchOrFail(grupoId);

        return permissionModelAssembler.toCollectionModel(grupo.getPermissions());
    }

    @PutMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associate(@PathVariable Long grupoId, @PathVariable Long permissionId){
        registerGrupo.associate(grupoId, permissionId);
    }

    @DeleteMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disassociate(@PathVariable Long grupoId, @PathVariable Long permissionId){
        registerGrupo.disassociate(grupoId, permissionId);
    }
}