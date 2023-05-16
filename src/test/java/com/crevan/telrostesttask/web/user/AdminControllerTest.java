package com.crevan.telrostesttask.web.user;

import com.crevan.telrostesttask.model.Role;
import com.crevan.telrostesttask.model.User;
import com.crevan.telrostesttask.repository.UserRepository;
import com.crevan.telrostesttask.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.crevan.telrostesttask.web.user.AdminController.REST_URL;
import static com.crevan.telrostesttask.web.user.UserTestData.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest extends AbstractControllerTest {

    static final String REST_URL_SLASH = REST_URL + "/";

    @Autowired
    private UserRepository repository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin, user));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "by-email?email=" + admin.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + ADMIN_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        User user = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(user, "newPass")))
                .andExpect(status().isCreated());
        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        user.setId(newId);
        USER_MATCHER.assertMatch(created, user);
        USER_MATCHER.assertMatch(repository.getExisted(newId), user);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createInvalid() throws Exception {
        User invalid = new User(null, null, null, null, "", "newpas", "phone", LocalDate.now(), Role.USER);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(invalid, "newpas")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        User updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, "newPass")))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(repository.getExisted(USER_ID), getUpdated());

    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        User invalid = new User(user);
        invalid.setSurname("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(invalid, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        User updated = new User(user);
        updated.setEmail(ADMIN_MAIL);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + USER_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(USER_ID).isPresent());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
