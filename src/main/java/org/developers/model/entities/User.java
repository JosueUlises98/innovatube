package org.developers.model.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_name", columnList = "user_name"),
        @Index(name = "idx_email_hash", columnList = "email_hash"),
        @Index(name = "idx_is_active_created_at", columnList = "is_active, created_at")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userid;
    @Column(name = "name", unique = true, nullable = false, length = 50)
    @Setter
    @Getter
    private String name;
    @Column(name = "lastname", unique = true, nullable = false, length = 100)
    @Setter
    @Getter
    private String lastname;
    @Column(name = "user_name", nullable = false)
    @Setter
    @Getter
    private String username;
    @Column(name = "password_hash", nullable = false)
    @Setter
    @Getter
    private String password;
    @Column(name = "email_hash", nullable = false)
    @Setter
    @Getter
    private String email;
    @Column(name = "created_at", nullable = false)
    @Setter
    @Getter
    private LocalDateTime createdAt;
    @Column(name = "last_login", nullable = false)
    @Setter
    @Getter
    private LocalDateTime lastLogin;
    @Column(name = "is_active", nullable = false)
    @Setter
    @Getter
    private Boolean isActive;
    // Relación One-to-Many con Favorites
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Favorite> favorites;
}
