package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.BusinessException;
import com.algaworks.algafood.domain.exception.UserNotFoundException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.User;
import com.algaworks.algafood.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RegisterUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterGrupoService registerGrupo;

    @Transactional
    public User save(User user){
        userRepository.detach(user);

        Optional<User> userExist = userRepository.findByEmail(user.getEmail());

        if (userExist.isPresent() && !userExist.get().equals(user)){
            throw new BusinessException(String.format("Already exist user registered with email %s", user.getEmail()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String actualPassword, String newPassword){
        User user = searchOrFail(userId);

        if (user.notSamePassword(actualPassword)){
            throw new BusinessException("Current password entered does not match the user's password.");
        }
        user.setPassword(newPassword);
    }

    public void disassociate(Long userId, Long grupoId){
        User user = searchOrFail(userId);
        Grupo grupo = registerGrupo.searchOrFail(grupoId);

        user.removeGrupo(grupo);
    }

    public void associate(Long userId, Long grupoId){
        User user = searchOrFail(userId);
        Grupo grupo = registerGrupo.searchOrFail(grupoId);

        user.addGrupo(grupo);
    }

    public User searchOrFail(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
