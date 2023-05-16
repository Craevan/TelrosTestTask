package com.crevan.telrostesttask.web.user;

import com.crevan.telrostesttask.model.User;
import com.crevan.telrostesttask.util.validation.ValidationUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController extends AbstractUserController {

    static final String REST_URL = "/api/admin/users";

    @GetMapping
    public List<User> getAll() {
        log.info("[AdminController:] Getting all users");
        return repository.findAll();
    }

    @GetMapping("/by-email")
    public User getByEmail(@RequestParam final String email) {
        log.info("[AdminController:] Get user with email={}", email);
        return repository.getExistedByEmail(email);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable final int id) {
        log.info("[AdminController:] Get user with ID={}", id);
        return repository.getExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody final User user) {
        log.info("[AdminController:] Create {}", user);
        ValidationUtil.checkNew(user);
        ValidationUtil.checkRoles(user);
        User created = repository.prepareAndSave(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody final User user, @PathVariable final int id) {
        log.info("[AdminController:] Updating {} with id={}", user, id);
        ValidationUtil.assureIdConsistent(user, id);
        repository.prepareAndSave(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final int id) {
        log.info("[AdminController:] Delete user with ID={}", id);
        repository.deleteExisted(id);
    }
}
