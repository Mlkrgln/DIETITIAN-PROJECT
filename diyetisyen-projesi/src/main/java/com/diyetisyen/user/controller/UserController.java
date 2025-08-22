package com.diyetisyen.user.controller;

import com.diyetisyen.common.exception.UserNotFoundException;
import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.mapper.UserMapper;
import com.diyetisyen.user.model.UserDto;
import com.diyetisyen.user.service.RoleService;
import com.diyetisyen.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("ID " + id + " için kullanıcı bulunamadı."));
        return ResponseEntity.ok(userMapper.toDto(user));
    }
    // Tüm kullanıcıları listele
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = List.copyOf(userService.findAllUsers());
        return ResponseEntity.ok(users);
    }

    // Yeni kullanıcı ekle
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Kullanıcı adı zaten mevcut.");
        }
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email zaten kayıtlı.");
        }

        // Varsayılan ROLE_USER rolü ekleyelim örnek olarak
        Optional<Role> userRole = roleService.findByName("ROLE_USER");
        if (userRole.isEmpty()) {
            return ResponseEntity.badRequest().body("ROLE_USER bulunamadı.");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(userRole.get());
        user.setRoles(roles);

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Kullanıcıya rol ekle
    @PostMapping("/{userId}/roles/{roleName}")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        Optional<User> userOpt = userService.findByUsername(userId.toString()); // Burada id ile arama olmadığı için userRepository’da id ile findById gerek
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();

        Optional<Role> roleOpt = roleService.findByName(roleName);
        if (roleOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Rol bulunamadı.");
        }
        user.getRoles().add(roleOpt.get());
        userService.saveUser(user);

        return ResponseEntity.ok("Rol başarıyla eklendi.");
    }

    // Kullanıcı sil
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("Kullanıcı silindi.");
    }
}