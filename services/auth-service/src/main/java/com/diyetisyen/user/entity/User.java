package com.diyetisyen.user.entity;

import com.diyetisyen.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true) // <-- üst sınıf alanlarını da dahil ettik
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // hashed

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toSet());
    }

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

