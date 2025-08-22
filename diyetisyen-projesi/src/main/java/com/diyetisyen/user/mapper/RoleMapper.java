package com.diyetisyen.user.mapper;

import com.diyetisyen.user.entity.Role;
import com.diyetisyen.user.model.RoleDto;

public class RoleMapper {

    public static RoleDto toDto(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }

    public static Role toEntity(RoleDto dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }
}