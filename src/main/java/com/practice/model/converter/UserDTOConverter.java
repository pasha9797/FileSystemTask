package com.practice.model.converter;

import com.practice.model.User;
import com.practice.model.UserPermission;
import com.practice.model.dto.UserDTO;

public class UserDTOConverter {
    public static UserDTO convertToDTO(User user, Iterable<UserPermission> permissions){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(dto.getUsername());
        for(UserPermission userPermission: permissions){
            dto.getPermissions().add(userPermission.getPermission().getName());
        }
        return dto;
    }
}
