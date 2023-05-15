package com.crevan.telrostesttask.model;

import com.crevan.telrostesttask.util.validation.NoHtml;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
@ToString(callSuper = true, exclude = "password")
public class User extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NoHtml
    @NotBlank
    @Size(max = 128)
    @Column(name = "surname")
    private String surname;

    @NoHtml
    @NotBlank
    @Size(max = 128)
    @Column(name = "firstname")
    private String firstname;

    @NoHtml
    @NotBlank
    @Size(max = 128)
    @Column(name = "patronymic")
    private String patronymic;

    @NotBlank
    @Size(max = 256)
    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Email
    @NoHtml
    @NotBlank
    @Size(max = 128)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}, name = "users_roles_unique"))
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(final User user) {
        this(user.id(), user.surname, user.firstname, user.patronymic, user.password, user.email, user.phoneNumber, user.birthdate, user.roles);
    }

    public User(
            final Integer id, final String surname, final String firstname, final String patronymic, final String password,
            final String email, final String phoneNumber, final LocalDate birthdate, final Role role) {
        super(id);
        this.surname = surname;
        this.firstname = firstname;
        this.patronymic = patronymic;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.roles = Set.of(role);
    }

    public User(
            final Integer id, final String surname, final String firstname, final String patronymic, final String password,
            final String email, final String phoneNumber, final LocalDate birthdate, final Collection<Role> roles) {
        super(id);
        this.surname = surname;
        this.firstname = firstname;
        this.patronymic = patronymic;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        setRoles(roles);
    }

    public void setRoles(final Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    public boolean hasRole(final Role role) {
        return roles != null && roles.contains(role);
    }
}
