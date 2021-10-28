package com.ss.training.utopia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private Integer id;

    @NotBlank(message = "Roles must have a valid name (User, Agent, Admin).")
    @Size(max = 45, message = "Role names cannot be longer than 45 characters.")
    private String roleName;
}
