package org.developers.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {@Index(
                name = "idx_username",
                columnList = "username"
        ), @Index(
                name = "idx_email",
                columnList = "email"
        ), @Index(
                name = "idx_created_at",
                columnList = "created_at"
        )}
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "user_id"
    )
    private Long userid;
    @Column(
            name = "name",
            unique = true,
            nullable = false,
            length = 50
    )
    private String name;
    @Column(
            name = "lastname",
            unique = true,
            nullable = false,
            length = 100
    )
    private String lastname;
    @Column(
            name = "user_name",
            unique = true,
            nullable = false
    )
    private String username;
    @Column(
            name = "password_hash",
            nullable = false
    )
    private String password;
    @Column(
            name = "email_hash",
            unique = true,
            nullable = false
    )
    private String email;
    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;
    @Column(
            name = "last_login",
            nullable = false
    )
    private LocalDateTime lastLogin;
    @Column(
            name = "is_active",
            nullable = false
    )
    private Boolean isActive;
}
