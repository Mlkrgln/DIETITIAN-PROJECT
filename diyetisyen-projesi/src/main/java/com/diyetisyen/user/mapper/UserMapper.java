package com.diyetisyen.user.mapper;

import com.diyetisyen.user.dto.UserDto;
import com.diyetisyen.user.dto.RoleDto;
import com.diyetisyen.user.entity.User;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toDto(User u) {
        if (u == null) return null;
        UserDto d = new UserDto();
        d.setId(u.getId());
        d.setUsername(u.getUsername());
        d.setEmail(u.getEmail());
        if (u.getRoles() != null) {
            d.setRoles(u.getRoles().stream().map(RoleMapper::toDto).collect(Collectors.toSet()));
        }
        return d;
    }
}
