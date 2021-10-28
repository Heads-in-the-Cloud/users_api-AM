package com.ss.training.utopia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer id;

    @NotBlank(message = "Users must be tied to a role.")
    private Integer roleId;

    @NotBlank(message = "Users must be provided a Given Name.")
    @Size(max = 255, message = "Given names cannot be longer than 255 characters.")
    private String givenName;

    @NotBlank(message = "Users must be provided a Family Name.")
    @Size(max = 255, message = "Family names cannot be longer than 255 characters.")
    private String familyName;

    @NotBlank(message = "Users must be provided a username.")
    @Size(max = 45, message = "Username cannot be longer than 45 characters.")
    private String username;

    @NotBlank(message = "Users must be provided a password.")
    @Size(max = 255, message = "Password cannot be longer than 255 characters.")
    private String password;

    @NotBlank(message = "Users must be provided an email.")
    @Email(message = "Email does not fit email specifications.")
    @Size(max = 255, message = "Emails cannot be longer than 255 characters.")
    private String email;

    @NotBlank(message = "Users must be provided a phone number.")
    @Size(max = 255, message = "Phone numbers cannot be longer than 45 characters.")
    private String phone;
}
