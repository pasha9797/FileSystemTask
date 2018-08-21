package com.practice.controller;

import com.practice.model.dto.UserDTO;
import com.practice.model.request.LoginRequest;
import com.practice.model.request.RegistrationRequest;
import com.practice.security.SecurityService;
import com.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    /**
     * @api {get} /api/users Get all users
     * @apiName GetAllUsers
     * @apiPermission Admin
     * @apiDescription Get list of json objects representing each user in database.
     * @apiGroup Users
     * @apiSuccessExample Success response
     * [
     * {
     * "username": "test",
     * "permissions": ["read", "write"]
     * },
     * {
     * "username": "admin",
     * "permissions": ["read", "write","admin"]
     * }
     * ]
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    /**
     * @api {post} /api/users Sign up
     * @apiName UserSignUp
     * @apiPermission Anybody
     * @apiDescription Register new user in system
     * @apiGroup Users
     * @apiParam {String} username New username.
     * @apiParam {String} password New password.
     * @apiParamExample {json} Request example
     * {
     * "username": "test",
     * "password": "test"
     * }
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> signUp(@RequestBody RegistrationRequest registrationRequest) throws Exception {
        UserDTO userDTO = userService.addUser(registrationRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{username}").buildAndExpand(userDTO.getUsername()).toUri();
        return ResponseEntity.created(location).body(userDTO);
    }

    /**
     * @api {get} /api/users/:username Get user
     * @apiName GetUser
     * @apiPermission Authenticated user
     * @apiDescription Get data about user in json format.
     * @apiGroup Users
     * @apiParam {String} username User's username.
     * @apiSuccessExample Success response
     * {
     * "username": "test",
     * "permissions": ["read", "write"]
     * }
     */
    @RequestMapping(value = "/users/{username}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getUser(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.getUser(username));
    }

    /**
     * @api {delete} /api/users/:username Delete user
     * @apiName DeleteUser
     * @apiPermission Admin
     * @apiDescription Delete user from system.
     * @apiGroup Users
     * @apiParam {String} username User's username.
     */
    @RequestMapping(value = "/users/{username}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> deleteUser(@PathVariable String username) throws Exception {
        this.userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    /**
     * @api {post} /api/sessions Sign in
     * @apiName SignIn
     * @apiPermission Anybody
     * @apiDescription Authenticate user in system.
     * @apiGroup Auth
     * @apiParam {String} username User's username.
     * @apiParam {String} password User's password.
     * @apiParamExample {json} Request example
     * {
     * "username": "test",
     * "password": "test"
     * }
     * @apiSuccess (Success 200) {json} User Json object representing user authenticated.
     * @apiSuccessExample Success response
     * {
     * "username": "test",
     * "permissions": ["read", "write"]
     * }
     * @apiError (Bad Request 400) BadRequest Bad credentials
     */
    @RequestMapping(value = "/sessions", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> signIn(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws Exception {
        String token = securityService.signIn(loginRequest.getUsername(), loginRequest.getPassword());
        response.setHeader(securityService.TOKEN_HEADER, token);
        return ResponseEntity.ok(userService.getUser(securityService.getSignedInUsername()));
    }

    /**
     * @api {delete} /api/sessions Sign out
     * @apiName SignOut
     * @apiPermission Anybody
     * @apiDescription Perform user log out.
     * @apiGroup Auth
     */
    @RequestMapping(value = "/sessions", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
        securityService.logOut(request, response);
        return ResponseEntity.noContent().build();
    }

    /**
     * @api {get} /api/users/:username/permissions Get user permissions
     * @apiName GetPermissions
     * @apiPermission Authenticated user
     * @apiDescription Get list user permissions.
     * @apiParam {String} username User's username.
     * @apiGroup Users
     * @apiSuccessExample Success response
     * ["read","write"]
     */
    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getPermissions(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userService.getUserPermissions(username));
    }

    /**
     * @api {delete} /api/users/:username/permissions Delete user permission
     * @apiName DeletePermission
     * @apiPermission Admin
     * @apiDescription Delete user permission.
     * @apiParam {String} username User's username.
     * @apiParam {String} permission User's permission name.
     * @apiGroup Users
     */
    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> deletePermission(@PathVariable String username, @RequestParam String permission) throws Exception {
        userService.deleteUserPermission(username, permission);
        return ResponseEntity.noContent().build();
    }

    /**
     * @api {post} /api/users/:username/permissions Add user permission
     * @apiName AddPermission
     * @apiPermission Admin
     * @apiDescription Add user permission.
     * @apiParam {String} username User's username.
     * @apiParamExample {String} User permission specified in request body
     * write
     * @apiGroup Users
     */
    @RequestMapping(value = "/users/{username}/permissions", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> addPermission(@PathVariable String username, @RequestBody String permission) throws Exception {
        userService.addUserPermission(username, permission);
        return ResponseEntity.ok().build();
    }
}
