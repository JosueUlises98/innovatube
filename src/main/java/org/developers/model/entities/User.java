package org.developers.model.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userid;
    @Column(,name = "name", unique = true, nullable = false, length = 50)
    String name;
    @Column(name = "lastname",unique = true, nullable = false, length = 100)
    String lastname;
    @Column(name = "user_name", nullable = false)
    String username;
    @Column(name = "password_hash", nullable = false)
    String password;
    @Column(name = "email_hash", nullable = false)
    String email;
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
    @Column(name = "last_login", nullable = false)
    LocalDateTime lastLogin;
    @Column(name = "profile_picture", nullable = false)
    String profilePicture;
    // Relación One-to-Many con Favorites
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Favorite> favorites;
}
