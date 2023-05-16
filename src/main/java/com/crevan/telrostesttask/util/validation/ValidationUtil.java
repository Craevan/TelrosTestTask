package com.crevan.telrostesttask.util.validation;

import com.crevan.telrostesttask.HasId;
import com.crevan.telrostesttask.error.IllegalRequestDataException;
import com.crevan.telrostesttask.model.Role;
import com.crevan.telrostesttask.model.User;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(final HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(final HasId bean, final int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must have id=" + id);
        }
    }

    public static void checkRoles(final User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }
    }
}
