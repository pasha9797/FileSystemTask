package com.practice.service;

import com.google.common.hash.Hashing;
import com.practice.exception.*;
import com.practice.model.Permission;
import com.practice.model.User;
import com.practice.model.converter.UserDTOConverter;
import com.practice.model.dto.UserDTO;
import com.practice.model.request.RegistrationRequest;
import com.practice.repository.PermissionRepository;
import com.practice.repository.UserRepository;
import com.practice.utils.PropertiesParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PermissionRepository permissionRepository;

    public UserService() {

    }

    public Iterable<UserDTO> getUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(UserDTOConverter.convertToDTO(user));
        }
        return dtos;
    }

    public UserDTO getUser(String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UserNotFoundException(username);
        return UserDTOConverter.convertToDTO(user);
    }

    public UserDTO addUser(RegistrationRequest registrationRequest) throws Exception {
        if (registrationRequest.getUsername().length() < PropertiesParser.getInstance().getMinUsernameLength())
            throw new ShortUsernameException(registrationRequest.getUsername());

        User user = userRepository.findByUsername(registrationRequest.getUsername());
        if (user != null)
            throw new UserAlreadyExistsException(registrationRequest.getUsername());

        user = new User();
        user.setUsername(registrationRequest.getUsername());
        String hashedPassword = Hashing.sha256()
                .hashString(registrationRequest.getPassword(), StandardCharsets.UTF_8)
                .toString();
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
