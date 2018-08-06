package com.practice.model;

import com.practice.service.FileSystemService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Date;

public class FileDTOConverter {
    public static FileDTO convertToDTO(File file, String rootDirectory) throws IOException {
        FileDTO dto;
        dto = new FileDTO();

        dto.setSize(file.length());

        dto.setPath(FileSystemService.getRelativePath(file.getCanonicalPath(), rootDirectory));

        Path path = Paths.get(file.getCanonicalPath());
        BasicFileAttributes attr;
        attr = Files.readAttributes(path, BasicFileAttributes.class);
        dto.setCreationDate(new Date(attr.creationTime().toMillis()));
        dto.setLastModifiedDate(new Date(attr.lastModifiedTime().toMillis()));
        dto.setLastAccessDate(new Date(attr.lastAccessTime().toMillis()));
        dto.setDirectory(file.isDirectory());

        return dto;
    }

    /*private static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }*/
}
