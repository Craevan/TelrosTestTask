package com.crevan.telrostesttask.web.user;

import com.crevan.telrostesttask.dto.UserTo;
import com.crevan.telrostesttask.model.Role;
import com.crevan.telrostesttask.model.User;
import com.crevan.telrostesttask.util.json.JsonUtil;
import com.crevan.telrostesttask.web.MatcherFactory;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class UserTestData {

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "password");

    public static final int ADMIN_ID = 1;
    public static final int USER_ID = 2;

    public static final int NOT_FOUND = 100;
    public static final String ADMIN_MAIL = "admin@gmail.com";
    public static final String USER_MAIL = "user@gmail.com";

    public static final User admin = new User(
            ADMIN_ID, "Adminskiy", "Admin", "Adminovich", "admin", ADMIN_MAIL,
            "79001112233", LocalDate.of(1988, 8, 26), Role.ADMIN);

    public static final User user = new User(
            USER_ID, "Userov", "User", "Userovich", "password", USER_MAIL,
            "79002223344", LocalDate.of(2000, 2, 2), Role.USER);

    public static User getNew() {
        return new User(null, "New surname", "New name", "New patronymic",
                "newPass", "new@gmail.com",
                "79001002030", LocalDate.now(), Role.USER);
    }

    public static UserTo getNewTo() {
        return new UserTo(null, "test@test.ru", "newPwd", "newName",
                "newFirstName", "newPatron", "79219876543", LocalDate.of(1992, 1, 2));
    }

    public static UserTo getUpdatedTo() {
        return new UserTo(null, USER_MAIL, "newPassword", "UpdatedSurName", "UpdatedFirstName",
                "Userovich", "79002223344", LocalDate.of(2000, 2, 2));
    }

    public static User getUpdated() {
        return new User(
                USER_ID, "Updated surName", "Updated Name", "Updated patronymic",
                "newPass", USER_MAIL, "79002223344",
                LocalDate.of(2000, 2, 2), Role.USER);
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
