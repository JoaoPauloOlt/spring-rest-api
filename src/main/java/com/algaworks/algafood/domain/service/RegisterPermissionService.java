package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.PermissionNotFoundException;
import com.algaworks.algafood.domain.model.Permission;
import com.algaworks.algafood.domain.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Permission searchOrFail(Long permissionId){
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new PermissionNotFoundException(permissionId));
    }
}