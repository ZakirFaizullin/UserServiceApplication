package org.example.userserviceapplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность пользователя")
public class UserDto {

    @Schema(description = "Уникальный идентификатор пользователя", example = "123")
    private Long id;
    @Schema(description = "ФИО", example = "Иванов Иван Иванович")
    private String name;
    @Schema(description = "Возраст", example = "25")
    private int age;
    @Schema(description = "Электронная почта пользователя", example = "ivan@mail.ru")
    private String email;
    @Schema(description = "Дата и время регистрации пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

}