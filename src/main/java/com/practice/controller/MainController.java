package com.practice.controller;

import com.practice.model.FileDTO;
import com.practice.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@EnableWebMvc
@RequestMapping(value = "/api")
public class MainController {
    private FileSystemService fileSystemService;

    public MainController(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> printHello() {
        return ResponseEntity.ok("hello from spring!");
    }

    @RequestMapping(value = "/get-file-info", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getFileInfo(@RequestParam String path) {
        try {
            return ResponseEntity.ok(fileSystemService.getFileDTO(path));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/get-text-file-content", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getTextFileContent(@RequestParam String path) {
        try {
            return ResponseEntity.ok(fileSystemService.readTextFile(path));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/remove-file", method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<?> removeFile(@RequestParam String path) {
        try {
            fileSystemService.removeFile(path);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private static class RenameRequest {
        private String path;
        private String newName;

        String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        String getNewName() {
            return newName;
        }

        public void setNewName(String newName) {
            this.newName = newName;
        }
    }

    @RequestMapping(value = "/rename-file", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> renameFile(@RequestBody RenameRequest renameRequest) {
        try {
            fileSystemService.renameFile(renameRequest.getPath(), renameRequest.getNewName());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private @ResponseBody static class MoveRequest {
        private String path;
        private String newPath;
        private Boolean keepOld;

        String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        String getNewPath() {
            return newPath;
        }

        public void setNewPath(String newPath) {
            this.newPath = newPath;
        }

        Boolean getKeepOld() {
            return keepOld;
        }

        public void setKeepOld(Boolean keepOld) {
            this.keepOld = keepOld;
        }
    }

    @RequestMapping(value = "/move-file", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> moveFile(@RequestBody MoveRequest moveRequest) {
        try {
            fileSystemService.moveFile(moveRequest.getPath(), moveRequest.getNewPath(), moveRequest.getKeepOld());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
