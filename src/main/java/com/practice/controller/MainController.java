package com.practice.controller;

import com.practice.service.FileSystemService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

@RestController
@EnableWebMvc
@RequestMapping(value = "/api")
public class MainController {
    private FileSystemService fileSystemService;

    public MainController(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @RequestMapping(value = "/get-file-info", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getFileInfo(@RequestParam String path) {
        try {
            return ResponseEntity.ok(fileSystemService.getFileDTO(path));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/get-directory-content", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getDirectoryContent(@RequestParam String path) {
        try {
            return ResponseEntity.ok(fileSystemService.getDirectoryContent(path));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/get-text-file-content", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getTextFileContent(@RequestParam String path) {
        try {
            return ResponseEntity.ok(fileSystemService.readTextFile(path));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/remove-file", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> removeFile(@RequestParam String path) {
        try {
            fileSystemService.removeFile(path);
            return ResponseEntity.ok().body("File removed successfully");
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
    public @ResponseBody
    ResponseEntity<String> renameFile(@RequestBody RenameRequest renameRequest) {
        try {
            fileSystemService.renameFile(renameRequest.getPath(), renameRequest.getNewName());
            return ResponseEntity.ok().body("File renamed successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private @ResponseBody
    static class MoveRequest {
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
    public @ResponseBody
    ResponseEntity<String> moveFile(@RequestBody MoveRequest moveRequest) {
        HttpHeaders h = new HttpHeaders();
        h.add("Content-type", "text/plain;charset=UTF-8");
        try {
            String resultPath = fileSystemService.moveFile(moveRequest.getPath(), moveRequest.getNewPath(), moveRequest.getKeepOld());
            return new ResponseEntity<>(resultPath, h, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), h, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/upload-file", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("directoryPath") String directoryPath) {
        try {
            return ResponseEntity.ok(fileSystemService.uploadFile(file, directoryPath));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/create-directory", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> createDirectory(@RequestParam String directoryPath) {
        try {

            return ResponseEntity.ok(fileSystemService.createDirectory(directoryPath));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
