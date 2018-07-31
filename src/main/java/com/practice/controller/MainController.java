package com.practice.controller;

import com.practice.model.FileDTO;
import com.practice.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api")
public class MainController {
    @Autowired
    FileSystemService fileSystemService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> printHello() {
        return ResponseEntity.ok("hello from spring!");
    }

    @RequestMapping(value = "/get-file-info", method = RequestMethod.GET)
    public ResponseEntity<?> getFileInfo(@RequestParam String path) {
        try {
            return ResponseEntity.ok(fileSystemService.getFileDTO(path));
        }
        catch(FileNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(IOException e){
            return ResponseEntity.badRequest().body("Error while trying to access file or directory");
        }
    }

    @RequestMapping(value = "/get-text-file-content", method = RequestMethod.GET)
    public ResponseEntity<String> getTextFileContent(@RequestParam String path) {
        return ResponseEntity.ok("request for: " + path);
    }

    @RequestMapping(value = "/remove-file", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeFile(@RequestParam String path) {
        return ResponseEntity.ok("request for: " + path);
    }

    private static class RenameRequest {
        private String path;
        private String newName;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getNewName() {
            return newName;
        }

        public void setNewName(String newName) {
            this.newName = newName;
        }
    }

    @RequestMapping(value = "/rename-file", method = RequestMethod.POST)
    public ResponseEntity<String> renameFile(@RequestBody RenameRequest renameRequest) {
        return ResponseEntity.ok("request for: " + renameRequest.getPath() + ", new name: " + renameRequest.getNewName());
    }

    private static class MoveRequest {
        private String path;
        private String newPath;
        private Boolean keepOld;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getNewPath() {
            return newPath;
        }

        public void setNewPath(String newPath) {
            this.newPath = newPath;
        }

        public Boolean getKeepOld() {
            return keepOld;
        }

        public void setKeepOld(Boolean keepOld) {
            this.keepOld = keepOld;
        }
    }

    @RequestMapping(value = "/move-file", method = RequestMethod.POST)
    public ResponseEntity<String> moveFile(@RequestBody MoveRequest moveRequest) {
        return ResponseEntity.ok("request for: " + moveRequest.getPath() + ", new path: " + moveRequest.getNewPath());
    }
}
