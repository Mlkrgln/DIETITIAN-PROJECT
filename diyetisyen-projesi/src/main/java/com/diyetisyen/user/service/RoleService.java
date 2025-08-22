package com.diyetisyen.user.service;

import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Set<Role> findAllRoles() {
        return Set.copyOf(roleRepository.findAll());
    }

    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }
}
