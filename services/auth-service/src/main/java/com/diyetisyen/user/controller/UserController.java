package com.diyetisyen.user.controller;

import com.diyetisyen.user.dto.UserDto;
import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.mapper.UserMapper;
import com.diyetisyen.user.service.RoleService;
import com.diyetisyen.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(org.springframework.security.core.Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        String username = auth.getName();
        Optional<User> u = userService.findByUsername(username);
        return u.map(user -> ResponseEntity.ok(UserMapper.toDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserMapper.toDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add role to user (admin)
    @PostMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addRole(@PathVariable Long userId, @PathVariable String roleName) {
        var userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();
        var roleOpt = roleService.findByName(roleName);
        if (roleOpt.isEmpty()) return ResponseEntity.badRequest().body("Role not found");
        User u = userOpt.get();
        Set<Role> roles = u.getRoles();
        roles.add(roleOpt.get());
        u.setRoles(roles);
        userService.save(u);
        return ResponseEntity.ok("Role added");
    }
}
