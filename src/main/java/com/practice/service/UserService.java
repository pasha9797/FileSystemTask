package com.practice.service;

import com.practice.exception.*;
import com.practice.model.Permission;
import com.practice.model.User;
import com.practice.model.dto.UserDTO;
import com.practice.model.request.RegistrationRequest;
import com.practice.repository.PermissionRepository;
import com.practice.repository.UserRepository;
import com.practice.security.Sha256Encoder;
import com.practice.utils.PropertiesParser;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    @Qualifier("mapper")
    DozerBeanMapper mapper;

    public UserService() {

    }

    public Iterable<UserDTO> getUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(mapper.map(user,UserDTO.class));
        }
        return dtos;
    }

    public UserDTO getUser(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException(username);
        return mapper.map(user,UserDTO.class);
    }

    public UserDTO addUser(RegistrationRequest registrationRequest) throws Exception {
        if (registrationRequest.getUsername().length() < PropertiesParser.getInstance().getMinUsernameLength())
            throw new ShortUsernameException(registrationRequest.getUsername());

        User user = userRepository.findByUsername(registrationRequest.getUsername());
        if (user != null)
            throw new UserAlreadyExistsException(registrationRequest.getUsername());

        user = new User();
        user.setUsername(registrationRequest.getUsername());
        String hashedPassword = new Sha256Encoder().encode(registrationRequest.getPassword());
        user.setPassword(hashedPassword);

        List<Permission> permissions = new ArrayList<>();
        String[] permissionsNames;
        try {
            PropertiesParser propertiesParser = PropertiesParser.getInstance();
            permissionsNames = propertiesParser.getInitialPermissions();
        } catch (Exception e) {
            permissionsNames = new String[]{};
            e.printStackTrace();
        }
        for (String permissionName : permissionsNames) {
            Permission permission = permissionRepository.findByName(permissionName);
            if (permission != null) {
                permissions.add(permission);
            }
        }
        user.setPermissions(permissions);

        userRepository.save(user);
        return getUser(registrationRequest.getUsername());
    }

    public void deleteUser(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException(username);
        userRepository.delete(user);
    }

    public Iterable<String> getUserPermissions(String username) throws Exception {
        return getUser(username).getPermissions();
    }

    public void addUserPermission(String username, String permissionName) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException(username);

        Permission permission = permissionRepository.findByName(permissionName);
        if (permission == null)
            throw new PermissionNotFoundException(permissionName);

        user.getPermissions().add(permission);
        userRepository.save(user);
    }

    public void deleteUserPermission(String username, String permissionName) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException(username);

        Permission permission = user.getPermissions().stream()
                .filter(per -> per.getName().equals(permissionName))
                .findAny()
                .orElse(null);

        if (permission == null)
            throw new NoUserPermissionException(username, permissionName);

        user.getPermissions().remove(permission);
        userRepository.save(user);
    }
}
