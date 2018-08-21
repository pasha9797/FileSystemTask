package com.practice.controller;

import com.practice.model.dto.FileDTO;
import com.practice.model.request.UpdateFileRequest;
import com.practice.service.FileSystemService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api")
public class FileSystemController {
    private FileSystemService fileSystemService;

    public FileSystemController(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    /**
     * @api {get} /api/files Get file or directory description
     * @apiName GetFileInfo
     * @apiDescription Get information about file or directory in json format.
     * @apiGroup Files
     * @apiParam {String} path Path to the file.
     * @apiSuccessExample Success response
     * {
     * "path": "Desktop",
     * "size": 4096,
     * "creationDate": 1529534686391,
     * "lastModifiedDate": 1533468452927,
     * "lastAccessDate": 1533468452927,
     * "directory": true
     * }
     */
    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> getFileInfo(@RequestParam String path) throws Exception {
        return ResponseEntity.ok(fileSystemService.getFileDTO(path));
    }

    /**
     * @api {get} /api/files/content Get file or directory content
     * @apiName GetFileContent
     * @apiDescription Get content of text file or content of directory.
     * @apiGroup Files
     * @apiParam {String} path Path to the file. If specified file is directory, response will contain array of json objects describing each file within directory. If specified file is text file, response will contain plain text from file.
     * @apiSuccessExample Success Directory content
     * [
     * {
     * "path": ".android",
     * "size": 0,
     * "creationDate": 1529564019132,
     * "lastModifiedDate": 1529564381823,
     * "lastAccessDate": 1529564381823,
     * "directory": true
     * },
     * {
     * "path": ".bash_history",
     * "size": 75,
     * "creationDate": 1533217156161,
     * "lastModifiedDate": 1533218627613,
     * "lastAccessDate": 1533217156161,
     * "directory": false
     * }
     * ]
     * @apiSuccessExample Success Text file
     * Content-Type: text/plain;charset=ISO-8859-1
     * Content of the file will follow here.
     */
    @RequestMapping(value = "/files/content", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<?> getFileContent(@RequestParam String path) throws Exception {
        Object response = fileSystemService.getFileContent(path);
        HttpHeaders headers= new HttpHeaders();
        if(response instanceof String){
            headers.add("Content-type","text/plain;charset=utf-8");
        }
        else{
            headers.add("Content-type","application/json;charset=utf-8");
        }
        return new ResponseEntity<>(response,headers,HttpStatus.OK);
    }

    /**
     * @api {delete} /api/files Delete file or directory
     * @apiName DeleteFile
     * @apiDescription Delete specified file or directory.
     * @apiGroup Files
     * @apiParam {String} path Path to the file.
     */
    @RequestMapping(value = "/files", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> removeFile(@RequestParam String path) throws Exception {
        fileSystemService.removeFile(path);
        return ResponseEntity.noContent().build();
    }

    /**
     * @api {put} /api/files Update file or directory
     * @apiName UpdateFile
     * @apiDescription Update file or directory according to specified option.
     * @apiGroup Files
     * @apiParam {String} path Path to the file.
     * @apiParam {String} newPath New path to the file - where to move/copy file/
     * @apiParam {String="COPY","MOVE"} option Option specifying what to do with file.
     * @apiParamExample {json} Request example
     * {
     * "path": "Desktop/test.exe",
     * "newPath": "Desktop/prod.exe",
     * "option": "MOVE"
     * }
     */
    @RequestMapping(value = "/files", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8")
    public @ResponseBody
    ResponseEntity<String> updateFile(@RequestBody UpdateFileRequest fileUpdateRequest) throws Exception {
        String resultPath = "";
        switch (fileUpdateRequest.getOption()) {
            case COPY:
                resultPath = fileSystemService.copyFile(fileUpdateRequest.getPath(), fileUpdateRequest.getNewPath());
                break;
            case MOVE:
                resultPath = fileSystemService.moveFile(fileUpdateRequest.getPath(), fileUpdateRequest.getNewPath());
                break;
        }
        return new ResponseEntity<>(resultPath, HttpStatus.OK);
    }

    /**
     * @api {post} /api/files Create file or directory
     * @apiName CreateFile
     * @apiDescription Upload new file or create a directory.
     * @apiGroup Files
     * @apiParam {String} directoryPath Path to directory where to upload new file, or path to the directory to create.
     * @apiParam {File} [file] File to upload. If not set, creation of directory at path directoryPath will be attempted.
     */
    @RequestMapping(value = "/files", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public @ResponseBody
    ResponseEntity<?> uploadFile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("directoryPath") String directoryPath
    ) throws Exception {
        FileDTO uploadedFile;

        if (file != null) {
            uploadedFile = fileSystemService.uploadFile(file, directoryPath);
        } else {
            uploadedFile = fileSystemService.createDirectory(directoryPath);
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().queryParam("path", uploadedFile.getPath()).build().toUri();
        return ResponseEntity.created(location).body(uploadedFile);
    }
}
