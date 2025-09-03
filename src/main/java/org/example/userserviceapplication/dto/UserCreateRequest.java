package org.example.userserviceapplication.dto;


import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Запрос")
public class UserCreateRequest {
    @NotBlank(message = "Name is required.")
    @Schema(description = "ФИО", example = "Иванов Иван Иванович")
    private String name;

    @NotNull(message = "Age is required.")
    @Schema(description = "Возраст", example = "25")
    private int age;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is required.")
    @Schema(description = "Электронная почта пользователя", example = "ivan@mail.ru")
    private String email;

}
