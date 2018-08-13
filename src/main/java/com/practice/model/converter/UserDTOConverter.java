package com.practice.model.converter;

import com.practice.model.Permission;
import com.practice.model.User;
import com.practice.model.dto.UserDTO;

public class UserDTOConverter {
    public static UserDTO convertToDTO(User user){
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        for(Permission permission: user.getPermissions()){
            dto.getPermissions().add(permission.getName());
        }
        return dto;
    }
}
