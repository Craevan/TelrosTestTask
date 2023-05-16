package com.crevan.telrostesttask.dto;

import com.crevan.telrostesttask.HasId;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserTo implements HasId {

    private Integer id;

    @Email
    @NotEmpty(message = "email can't be empty")
    private String email;

    @NotBlank
    @Size(max = 256)
    private String password;

    @NotBlank
    @Size(max = 128)
    private String surname;

    @NotBlank
    @Size(max = 128)
    private String firstname;

    @NotBlank
    @Size(max = 128)
    private String patronymic;

    @NotBlank
    @Pattern(regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$")
    private String phoneNumber;

    @NotNull
    private LocalDate birthdate;

    public UserTo(final Integer id, final String email, final String password, final String surname,
                  final String firstname, final String patronymic, final String phoneNumber, final LocalDate birthdate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.firstname = firstname;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
    }
}
