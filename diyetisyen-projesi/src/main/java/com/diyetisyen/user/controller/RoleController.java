package com.diyetisyen.user.controller;

import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService){ this.roleService = roleService; }

    @GetMapping
    public ResponseEntity<List<Role>> all() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.saveRole(role));
    }
}
