package com.practice.controller;

import com.practice.model.dto.UserDTO;
import com.practice.model.request.LoginRequest;
import com.practice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    UserService userService;

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
    ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok("test");
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUsers());
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> deleteUser(@PathVariable String username) {
        return ResponseEntity.ok("test");
    }

    @RequestMapping(value = "/users/{username}", method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity<?> updateUser(@PathVariable String username, @RequestParam UserDTO userDTO) {
        return ResponseEntity.ok("test");
    }

    @RequestMapping(value = "/users/sessions", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok("test");
    }

    @RequestMapping(value = "/users/sessions", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> signOut() {
        return ResponseEntity.ok("test");
    }

    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getPermissions(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUsers());
    }

    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> deletePermission(@PathVariable String username, @RequestParam String permission) {
        return ResponseEntity.ok("test");
    }

    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addPermission(@PathVariable String username, @RequestBody String permission) {
        return ResponseEntity.ok("test");
    }
}
