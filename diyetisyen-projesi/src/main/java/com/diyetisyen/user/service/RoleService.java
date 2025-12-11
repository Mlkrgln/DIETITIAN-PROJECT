package com.diyetisyen.user.service;

import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) { this.roleRepository = roleRepository; }

    public Role saveRole(Role r) { return roleRepository.save(r); }
    public Optional<Role> findByName(String name) { return roleRepository.findByName(name); }
    public List<Role> findAll() { return roleRepository.findAll(); }
}
