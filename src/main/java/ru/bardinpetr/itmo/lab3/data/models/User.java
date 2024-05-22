package ru.bardinpetr.itmo.lab3.data.models;

import jakarta.persistence.*;
import jakarta.security.enterprise.CallerPrincipal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User implements Serializable, Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String passwordHash;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private List<PointResult> pointResults = new ArrayList<>();

    @Override
    public String getName() {
        return getLogin();
    }
}
