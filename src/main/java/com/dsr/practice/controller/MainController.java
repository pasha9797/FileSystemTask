package com.dsr.practice.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.graalvm.compiler.lir.sparc.SPARCMove;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class MainController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> printHello() {
        return ResponseEntity.ok("hello from spring!");
    }

    @RequestMapping(value = "/get-directory-content", method = RequestMethod.GET)
    public ResponseEntity<String> getDirectoryContent(@RequestParam String path) {
        return ResponseEntity.ok("request for: "+path);
    }

    @RequestMapping(value = "/get-text-file-content", method = RequestMethod.GET)
    public ResponseEntity<String> getTextFileContent(@RequestParam String path) {
        return ResponseEntity.ok("request for: "+path);
    }

    @RequestMapping(value = "/remove-file", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeFile(@RequestParam String path) {
        return ResponseEntity.ok("request for: "+path);
    }

    private static class RenameRequest{
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
        return ResponseEntity.ok("request for: "+renameRequest.getPath()+", new name: "+renameRequest.getNewName());
    }

    private static class MoveRequest{
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
        return ResponseEntity.ok("request for: "+moveRequest.getPath()+", new path: "+moveRequest.getNewPath());
    }
}
