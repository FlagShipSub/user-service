package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
@Builder
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_address_id", referencedColumnName = "id")
    private UserAddress userAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isRemoved;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isEnabled;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles rolesAssigned;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "credential_id", referencedColumnName = "id")
    private RegisteredUserCredentials registeredUserCredentials;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAssigned.name().equals("ADMIN") ?
                Arrays.asList(
                        new SimpleGrantedAuthority(Roles.USER.name()),
                        new SimpleGrantedAuthority(Roles.ADMIN.name())
                ) :
                Collections.singleton(new SimpleGrantedAuthority(rolesAssigned.name()));
    }

    @Override
    public String getPassword() {
        return registeredUserCredentials.getPassword();
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
