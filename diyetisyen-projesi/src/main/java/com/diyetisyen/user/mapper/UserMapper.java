package com.diyetisyen.user.mapper;

import com.diyetisyen.user.entity.User;
import com.diyetisyen.user.model.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
//        dto.setRole(user.getRole());
        // başka alanlar varsa onları da ekle

        return dto;
    }

    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
//        user.setRole(dto.getRole());
        // varsa diğer alanlar

        return user;
    }
}
