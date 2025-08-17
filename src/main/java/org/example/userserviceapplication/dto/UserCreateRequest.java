package org.example.userserviceapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Age is required.")
    private int age;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is required.")
    private String email;

}
