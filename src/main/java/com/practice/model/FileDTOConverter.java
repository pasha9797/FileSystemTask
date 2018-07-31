package com.practice.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Date;
import java.util.ArrayList;

public class FileDTOConverter {
    public static FileDTO convertToDTO(File file) throws IOException {
        FileDTO dto;
        if (file.isFile()) {
            dto = new FileDTO();
        } else {
            dto = new DirectoryDTO();
            ((DirectoryDTO) dto).setContentFiles(new ArrayList<>());
            for (File child : file.listFiles())
                ((DirectoryDTO) dto).getContentFiles().add(child.getName());
        }

        dto.setSize(file.length());
        dto.setPath(file.getCanonicalPath());
        Path path = Paths.get(file.getCanonicalPath());
        BasicFileAttributes attr;
        attr = Files.readAttributes(path, BasicFileAttributes.class);
        dto.setCreationDate(new Date(attr.creationTime().toMillis()));
        dto.setLastModifiedDate(new Date(attr.lastModifiedTime().toMillis()));
        dto.setLastAccessDate(new Date(attr.lastAccessTime().toMillis()));

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
