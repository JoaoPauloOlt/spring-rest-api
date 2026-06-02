package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.api.assembler.UserInputDisassembler;
import com.algaworks.algafood.api.assembler.UserModelAssembler;
import com.algaworks.algafood.api.model.UserModel;
import com.algaworks.algafood.api.model.input.PasswordInput;
import com.algaworks.algafood.api.model.input.UserInput;
import com.algaworks.algafood.api.model.input.UserWithPasswordInput;
import com.algaworks.algafood.domain.model.User;
import com.algaworks.algafood.domain.repository.UserRepository;
import com.algaworks.algafood.domain.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterUserService registerUser;

    @Autowired
    private UserModelAssembler userModelAssembler;

    @Autowired
    private UserInputDisassembler userInputDisassembler;

    @GetMapping
    public List<UserModel> list(){
        List<User> allUsers = userRepository.findAll();

        return userModelAssembler.toCollectionModel(allUsers);
    }

    @GetMapping("/{userId}")
    public UserModel search(@PathVariable Long userId){
        User user = registerUser.searchOrFail(userId);

        return userModelAssembler.toModel(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel create(@RequestBody @Valid UserWithPasswordInput userInput){
        User user = userInputDisassembler.toDomainObject(userInput);
        user = registerUser.save(user);

        return userModelAssembler.toModel(user);
    }

    @PutMapping("/{userId}")
    public UserModel update(@PathVariable Long userId,
                            @RequestBody @Valid UserInput userInput){
        User actualUser = registerUser.searchOrFail(userId);
        userInputDisassembler.copyToDomainObject(userInput, actualUser);
        actualUser = registerUser.save(actualUser);

        return userModelAssembler.toModel(actualUser);
    }

    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable Long useId, @RequestBody @Valid PasswordInput password){
        registerUser.changePassword(useId, password.getActualPassword(), password.getNewPassword());
    }
}
