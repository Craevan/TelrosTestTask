package com.crevan.telrostesttask.repository;

import com.crevan.telrostesttask.error.NotFoundException;
import com.crevan.telrostesttask.model.Role;
import com.crevan.telrostesttask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.Optional;

import static com.crevan.telrostesttask.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :id")
    int delete(final int id);

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(final String email);

    @Transactional
    default User prepareAndSave(final User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(EnumSet.of(Role.USER));
        }
        return save(user);
    }

    default User getExistedByEmail(final String email) {
        return findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("User with email=" + email.toLowerCase() + " not found"));
    }

    default User getExisted(final int id) {
        return findById(id).orElseThrow(() -> new NotFoundException("User with ID=" + id + " was not found"));
    }

    default void deleteExisted(final int id) {
        if (delete(id) == 0) {
            throw new NotFoundException("User with ID=" + id + " was not found");
        }
    }
}
