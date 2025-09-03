package org.example.userserviceapplication.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность пользователя")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "123")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "ФИО", example = "Иванов Иван Иванович")
    private String name;

    @Column(nullable = false)
    @Schema(description = "Возраст", example = "25")
    private int age;

    @Column(unique = true, nullable = false)
    @Schema(description = "Электронная почта пользователя", example = "ivan@mail.ru")
    private String email;

    @Column(updatable = false)
    @Schema(description = "Дата и время регистрации пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

}
