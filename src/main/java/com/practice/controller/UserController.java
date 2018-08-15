package com.practice.controller;

import com.practice.exception.NoUserPermissionException;
import com.practice.exception.PermissionNotFoundException;
import com.practice.exception.UserAlreadyExistsException;
import com.practice.exception.UserNotFoundException;
import com.practice.model.dto.UserDTO;
import com.practice.model.request.LoginRequest;
import com.practice.model.request.RegistrationRequest;
import com.practice.security.SecurityService;
import com.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SecurityService securityService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> signUp(@RequestBody RegistrationRequest registrationRequest) throws Exception {
        UserDTO userDTO = userService.addUser(registrationRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{username}").buildAndExpand(userDTO.getUsername()).toUri();
        return ResponseEntity.created(location).body(userDTO);
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getUser(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> deleteUser(@PathVariable String username) throws Exception {
        this.userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) throws Exception {
        securityService.signIn(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(userService.getUser(securityService.getSignedInUsername()));
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
        securityService.logOut(request, response);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getPermissions(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.getUserPermissions(username));
    }

    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> deletePermission(@PathVariable String username, @RequestParam String permission) throws Exception {
        userService.deleteUserPermission(username, permission);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addPermission(@PathVariable String username, @RequestBody String permission) throws Exception {
        userService.addUserPermission(username, permission);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    String handleUserNotFound(Exception ex) {
        return "No such user exists: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PermissionNotFoundException.class)
    @ResponseBody
    String handlePermissionNotFound(Exception ex) {
        return "No such permission exists: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoUserPermissionException.class)
    @ResponseBody
    String handleNoUserPermission(NoUserPermissionException ex) {
        return "User " + ex.getUsername() + " does not have such permission: " + ex.getPermission();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseBody
    String handleUserAlreadyExists(Exception ex) {
        return "User with such name already exists: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    String handleDataAccessException(Exception ex) {
        return "Error happened trying to access data: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    String handleAuthenticationException(Exception ex) {
        return "Bad credentials";
    }
}
