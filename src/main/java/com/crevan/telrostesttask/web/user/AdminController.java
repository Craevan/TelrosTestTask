package com.crevan.telrostesttask.web.user;

import com.crevan.telrostesttask.error.SwaggerExceptionInfo;
import com.crevan.telrostesttask.model.User;
import com.crevan.telrostesttask.util.validation.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "Admin controller", description = "CRUD Controller for Administrators")
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = SwaggerExceptionInfo.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden",
                content = @Content(schema = @Schema(implementation = SwaggerExceptionInfo.class)))
})
public class AdminController extends AbstractUserController {

    static final String REST_URL = "/api/admin/users";

    @GetMapping
    @Operation(description = "Get all users", responses = {@ApiResponse(responseCode = "200", description = "Ok")})
    public List<User> getAll() {
        log.info("[AdminController:] Getting all users");
        return repository.findAll();
    }

    @GetMapping("/by-email")
    @Operation(description = "Get user by e-mail",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(schema = @Schema(implementation = SwaggerExceptionInfo.class)))
            })
    public User getByEmail(@RequestParam final String email) {
        log.info("[AdminController:] Get user with email={}", email);
        return repository.getExistedByEmail(email);
    }

    @GetMapping("/{id}")
    @Operation(description = "Get user by Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok"),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(schema = @Schema(implementation = SwaggerExceptionInfo.class)))
            })
    public User getById(@PathVariable final int id) {
        log.info("[AdminController:] Get user with ID={}", id);
        return repository.getExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                            content = @Content(schema = @Schema(implementation = SwaggerExceptionInfo.class)))
            })
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
    @Operation(description = "Update existed user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                            content = @Content(schema = @Schema(implementation = SwaggerExceptionInfo.class)))
            })
    public void update(@Valid @RequestBody final User user, @PathVariable final int id) {
        log.info("[AdminController:] Updating {} with id={}", user, id);
        ValidationUtil.assureIdConsistent(user, id);
        repository.prepareAndSave(user);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Delete user by id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "404", description = "Not Found",
                            content = @Content(schema = @Schema(implementation = SwaggerExceptionInfo.class))),
                    @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                            content = @Content(schema = @Schema(implementation = SwaggerExceptionInfo.class)))
            })
    public void delete(@PathVariable final int id) {
        log.info("[AdminController:] Delete user with ID={}", id);
        repository.deleteExisted(id);
    }
}
