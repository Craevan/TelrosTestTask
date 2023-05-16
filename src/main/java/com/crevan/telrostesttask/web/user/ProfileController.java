package com.crevan.telrostesttask.web.user;

import com.crevan.telrostesttask.dto.UserTo;
import com.crevan.telrostesttask.model.User;
import com.crevan.telrostesttask.util.UserUtil;
import com.crevan.telrostesttask.util.validation.ValidationUtil;
import com.crevan.telrostesttask.web.AuthUser;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController extends AbstractUserController {

    static final String REST_URL = "/api/profile";

    @GetMapping
    public User get(@AuthenticationPrincipal final AuthUser authUser) {
        log.info("[Profile Controller:] Get {}", authUser);
        return authUser.getUser();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@Valid @RequestBody final UserTo userTo) {
        log.info("[Profile Controller:] Register new user {}", userTo);
        ValidationUtil.checkNew(userTo);
        User newUser = repository.save(UserUtil.prepareToSave(UserUtil.createNewFromTo(userTo)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(newUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid final UserTo userTo, @AuthenticationPrincipal final AuthUser authUser) {
        log.info("[Profile Controller:] Update {} to {}", authUser, userTo);
        User user = authUser.getUser();
        repository.prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal final AuthUser authUser) {
        log.info("[Profile Controller:] Delete {}", authUser);
        repository.deleteExisted(authUser.id());
    }
}
