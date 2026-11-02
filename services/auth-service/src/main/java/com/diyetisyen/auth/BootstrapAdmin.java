package com.diyetisyen.auth;

import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.service.RoleService;
import com.diyetisyen.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import java.util.Set;

@Component
@Order(2)
public class BootstrapAdmin implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String email;
    private final String password;

    public BootstrapAdmin(
            UserService userService,
            RoleService roleService,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap-admin.username:}") String username,
            @Value("${app.bootstrap-admin.email:}") String email,
            @Value("${app.bootstrap-admin.password:}") String password) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run(String... args) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            return;
        }

        if (userService.findByUsername(username).isPresent()) {
            return;
        }

        Role adminRole = roleService.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleService.saveRole(Role.builder().name("ROLE_ADMIN").build()));
        User admin = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(Set.of(adminRole))
                .build();
        userService.save(admin);
    }
}
