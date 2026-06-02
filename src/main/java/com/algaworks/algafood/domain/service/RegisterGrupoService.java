package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntityInUseException;
import com.algaworks.algafood.domain.exception.GrupoNotFoundException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterGrupoService {

    private static final String MSG_GRUPO_IN_USE = "Code group %d cannot be removed as it is in use";

    @Autowired
    private GrupoRepository grupoRepository;

    @Transactional
    public Grupo save(Grupo grupo){
        return grupoRepository.save(grupo);
    }

    @Transactional
    public void delete(Long grupoId){
        try {
            grupoRepository.deleteById(grupoId);
            grupoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new GrupoNotFoundException(grupoId);
        } catch (DataIntegrityViolationException e){
            throw new EntityInUseException(String.format(MSG_GRUPO_IN_USE, grupoId));
        }
    }

    public Grupo searchOrFail(Long grupoId){
        return grupoRepository.findById(grupoId)
                .orElseThrow(() -> new GrupoNotFoundException(grupoId));
    }
}
