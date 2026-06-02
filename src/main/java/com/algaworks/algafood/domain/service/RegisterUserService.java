package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.BusinessException;
import com.algaworks.algafood.domain.exception.UserNotFoundException;
import com.algaworks.algafood.domain.model.User;
import com.algaworks.algafood.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(User user){
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

    public User searchOrFail(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
