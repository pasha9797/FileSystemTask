package com.practice.controller;

import com.practice.exception.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchFileException.class)
    @ResponseBody
    String handleNoSuchFile(Exception ex) {
        return "No such file or directory: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotDirectoryException.class)
    @ResponseBody
    String handleNotDirectory(Exception ex) {
        return "Specified file is not directory: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotTextFileException.class)
    @ResponseBody
    String handleNotTextFile(NotTextFileException ex) {
        return "Specified file is not text file: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(FileAlreadyExistsException.class)
    @ResponseBody
    String handleFileAlreadyExists(Exception ex) {
        return "File with such name already exists: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ForbiddenPathSymbolException.class)
    @ResponseBody
    String handleForbiddenPathSymbol(Exception ex) {
        return "Forbidden symbols found in file path: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(IOException.class)
    @ResponseBody
    String handleIOException(Exception ex) {
        return "Unknown error occurred trying to perform action on file: " + ex.getMessage();
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
    @ExceptionHandler(ShortUsernameException.class)
    @ResponseBody
    String handleShortUsername(Exception ex) {
        return "Username is too short: " + ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    String handleAuthenticationException(Exception ex) {
        return "Bad credentials";
    }
}
