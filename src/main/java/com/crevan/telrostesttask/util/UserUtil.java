package com.crevan.telrostesttask.util;

import com.crevan.telrostesttask.config.SecurityConfig;
import com.crevan.telrostesttask.dto.UserTo;
import com.crevan.telrostesttask.model.Role;
import com.crevan.telrostesttask.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserUtil {

    public static User createNewFromTo(final UserTo userTo) {
        return new User(null, userTo.getSurname(), userTo.getFirstname(), userTo.getPatronymic(), userTo.getPassword(),
                userTo.getEmail(), userTo.getPhoneNumber(), userTo.getBirthdate(), Role.USER);
    }

    public static User updateFromTo(final User user, final UserTo userTo) {
        user.setSurname(userTo.getSurname());
        user.setFirstname(userTo.getFirstname());
        user.setPatronymic(userTo.getPatronymic());
        user.setPassword(userTo.getPassword());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setBirthdate(userTo.getBirthdate());
        return user;
    }

    public static User prepareToSave(final User user) {
        user.setPassword(SecurityConfig.PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
