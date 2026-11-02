package com.diyetisyen.auth;

import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

@Component
@Order(1)
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleService roleService;

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_CUSTOMER");
        createRoleIfNotExists("ROLE_DIETITIAN");
    }

    private void createRoleIfNotExists(String roleName) {
        roleService.findByName(roleName)
                .orElseGet(() -> roleService.saveRole(
                        Role.builder()
                                .name(roleName)
                                .build()
                ));
    }
}
