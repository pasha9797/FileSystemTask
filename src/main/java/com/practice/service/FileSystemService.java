package com.practice.service;

import com.practice.model.FileDTO;
import com.practice.model.FileDTOConverter;
import org.springframework.stereotype.Service;
import sun.security.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemService {
    public FileSystemService() {

    }

    public FileDTO getFileDTO(String path) throws IOException {
        File file = openWithCheck(path);
        return FileDTOConverter.convertToDTO(file);
    }

    public String readTextFile(String path) throws IOException {
        File file = openWithCheck(path);

        String data;
        data = new String(Files.readAllBytes(file.toPath()));
        return data;
    }

    public void removeFile(String path) throws IOException {
        File file = openWithCheck(path);
        boolean result = removeFileRecursive(file);
        if (!result)
            throw new IOException("Unable to remove file or directory");
    }

    private boolean removeFileRecursive(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles())
                removeFileRecursive(child);
        }
        return file.delete();
    }

    public void renameFile(String path, String newName) throws IOException {
        File file = openWithCheck(path);
        String canonicalPath = file.getCanonicalPath();
        String newPath = canonicalPath.substring(0, canonicalPath.lastIndexOf('\\') + 1) + newName;
        File file2 = new File(newPath);

        if (file2.exists())
            throw new java.io.IOException("File or directory with such name already exists");

        boolean result = file.renameTo(file2);

        if (!result) {
            throw new IOException("Unable to rename file or directory");
        }
    }

    public void moveFile(String path, String newPath, Boolean keepOld) throws IOException {
        if (!keepOld) {
            Path temp = Files.move(Paths.get(path), Paths.get(newPath));
            if (temp == null) {
                throw new IOException("Unable to move file or directory");
            }
        }
        else{
            Path temp = Files.copy(Paths.get(path), Paths.get(newPath));
            if (temp == null) {
                throw new IOException("Unable to copy file or directory");
            }
        }
    }

    private File openWithCheck(String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            throw new FileNotFoundException("No such file or directory");
        return file;
    }
}
