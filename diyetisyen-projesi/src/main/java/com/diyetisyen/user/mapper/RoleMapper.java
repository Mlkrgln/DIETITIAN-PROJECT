package com.diyetisyen.user.mapper;

import com.diyetisyen.user.dto.RoleDto;
import com.diyetisyen.user.entity.Role;

public class RoleMapper {
    public static RoleDto toDto(Role r) {
        if (r == null) return null;
        RoleDto d = new RoleDto();
        d.setId(r.getId());
        d.setName(r.getName());
        return d;
    }
}
