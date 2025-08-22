package com.diyetisyen.user.controller;

import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    // Tüm rolleri listele
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = List.copyOf(roleService.findAllRoles());
        return ResponseEntity.ok(roles);
    }

    // Yeni rol oluştur
    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role savedRole = roleService.saveRole(role);
        return ResponseEntity.ok(savedRole);
    }

    // Rol sil
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok("Rol silindi.");
    }
}
