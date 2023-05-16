package com.crevan.telrostesttask.web.user;

import com.crevan.telrostesttask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public abstract class AbstractUserController {

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }
}
