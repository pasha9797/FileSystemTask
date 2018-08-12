package com.practice.service;

import com.practice.model.User;
import com.practice.model.UserPermission;
import com.practice.repository.PermissionRepository;
import com.practice.repository.UserPermissionRepository;
import com.practice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserPermissionRepository userPermissionRepository;
    @Autowired
    PermissionRepository permissionRepository;

    public UserService(){

    }

    public Iterable<User> getUsers(){
        return userRepository.findAll();
    }
}
