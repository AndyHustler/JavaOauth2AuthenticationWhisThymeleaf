package ru.greenatom.atsdb.model.database.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.greenatom.atsdb.model.enumeration.ERole;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, length = 20)
    private String userName;

    @Column(nullable = false, length = 20)
    private String login;

    @Column(nullable = false, length = 120)
    private String password;

    @ElementCollection(targetClass = ERole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<ERole> roles = new HashSet<>();

    private User(UserBuilder builder) {
        this.userName = builder.userName;
        this.password = builder.password;
        this.login = builder.login;
        this.roles = builder.roles;
    }

    public boolean hasRole(ERole role) {
        for(ERole r : this.roles) {
            if(role.equals(r)) return true;
        };
        return false;
    }

    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }

    public static class UserBuilder {
        private String userName;
        private String password;
        private String login;
        private Set<ERole> roles = new HashSet<>();

        public UserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder login(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder role(Set<ERole> roles) {
            this.roles.addAll(roles);
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }
}
