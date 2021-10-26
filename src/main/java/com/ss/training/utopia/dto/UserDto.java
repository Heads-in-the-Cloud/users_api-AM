package com.ss.training.utopia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private Integer roleId;
    private String givenName;
    private String familyName;
    private String username;
    private String password;
    private String email;
    private String phone;
}
