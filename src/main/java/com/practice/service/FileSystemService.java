package com.practice.service;

import com.practice.exception.ForbiddenPathSymbolException;
import com.practice.exception.NotTextFileException;
import com.practice.model.dto.FileDTO;
import com.practice.model.converter.FileDTOConverter;
import com.practice.utils.PropertiesParser;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileSystemService {
    private String rootDirectory;

    public FileSystemService() throws Exception{
        PropertiesParser parser = PropertiesParser.getInstance();
        try {
            rootDirectory = parser.getRootDirectory();
            File file = new File(rootDirectory);
            if (!file.exists())
                throw new NoSuchFileException(rootDirectory);
            if (!file.isDirectory())
                throw new NotDirectoryException(rootDirectory);
        } catch (Exception e) {
            e.printStackTrace();
            rootDirectory = "/";
        }
        rootDirectory=new File(rootDirectory).getCanonicalPath();
        rootDirectory += '/';
        rootDirectory = removeRepeatingSlashes(rootDirectory);
    }

    public FileDTO getFileDTO(String path) throws Exception {
        File file = openWithCheck(getAbsolutePath(path, rootDirectory));
        return FileDTOConverter.convertToDTO(file, rootDirectory);
    }

    public Object getFileContent(String path) throws Exception {
        File file = openWithCheck(getAbsolutePath(path, rootDirectory));
        if (file.isDirectory())
            return getDirectoryContent(file);
        else
            return readTextFile(file);
    }

    private List<FileDTO> getDirectoryContent(File directory) throws Exception {
        List<FileDTO> content = new ArrayList<FileDTO>();
        File[] children = directory.listFiles();
        if (children == null)
            throw new IOException(getRelativePath(directory.getCanonicalPath(), rootDirectory));
        for (File child : children) {
            content.add(FileDTOConverter.convertToDTO(child, rootDirectory));
        }

        return content;
    }

    private String readTextFile(File file) throws Exception {
        if (!(
                file.getName().endsWith(".txt") ||
                        file.getName().endsWith(".rtf") ||
                        file.getName().endsWith(".xml") ||
                        file.getName().endsWith(".html")
        ))
            throw new NotTextFileException(getRelativePath(file.getCanonicalPath(), rootDirectory));
        String data;
        data = new String(Files.readAllBytes(file.toPath()));
        return data;
    }

    public void removeFile(String path) throws Exception {
        File file = openWithCheck(getAbsolutePath(path, rootDirectory));
        boolean result = removeFileRecursive(file);
        if (!result)
            throw new IOException(path);
    }

    private boolean removeFileRecursive(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles())
                removeFileRecursive(child);
        }
        return file.delete();
    }

    /*public void renameFile(String path, String newName) throws IOException {
        if (hasForbiddenSymbols(newName))
            throw new IOException("Forbidden symbols found in new name");

        File file = openWithCheck(getAbsolutePath(path, rootDirectory));
        String canonicalPath = file.getCanonicalPath();
        String newPath = canonicalPath.substring(0, canonicalPath.lastIndexOf('\\') + 1) + newName;
        File file2 = new File(newPath);

        if (file2.exists())
            throw new FileAlreadyExistsException("File or directory with such name already exists");

        boolean result = file.renameTo(file2);

        if (!result) {
            throw new IOException("Unable to rename file or directory");
        }
    }*/

    public String moveFile(String path, String newPath) throws Exception {
        if (hasForbiddenSymbols(path))
            throw new ForbiddenPathSymbolException(path);
        if (hasForbiddenSymbols(newPath))
            throw new ForbiddenPathSymbolException(newPath);

        String absPath = getAbsolutePath(path, rootDirectory);
        String absNewPath = getAbsolutePath(newPath, rootDirectory);

        File srcFile = openWithCheck(absPath);
        File destFile = new File(absNewPath);
        String destFilePath = removeRepeatingSlashes(destFile.getCanonicalPath());
        String absDestDir = destFilePath.substring(0, destFilePath.lastIndexOf('/'));
        File destDir = new File(absDestDir);
        if (!destDir.exists())
            throw new NoSuchFileException(getRelativePath(destDir.getCanonicalPath(), rootDirectory));

        if (destFile.exists()) {
            throw new FileAlreadyExistsException(newPath);
        }

        try {
            if (!srcFile.isDirectory()) {
                Path temp = Files.move(srcFile.toPath(), destFile.toPath());
                if (temp == null) {
                    throw new IOException();
                }
            } else {
                FileUtils.moveDirectory(srcFile, destFile);
            }

            return getRelativePath(destFile.getCanonicalPath(), rootDirectory);
        } catch (Exception e) {
            throw new IOException(path);
        }
    }

    public String copyFile(String path, String newPath) throws Exception {
        if (hasForbiddenSymbols(path))
            throw new ForbiddenPathSymbolException(path);
        if (hasForbiddenSymbols(newPath))
            throw new ForbiddenPathSymbolException(newPath);

        String absPath = getAbsolutePath(path, rootDirectory);
        String absNewPath = getAbsolutePath(newPath, rootDirectory);

        File srcFile = openWithCheck(absPath);
        File destFile = new File(absNewPath);

        while (destFile.exists()) {
            int extensionDot = absNewPath.lastIndexOf('.');
            if (extensionDot >= 0) {
                absNewPath = new StringBuilder(absNewPath).insert(extensionDot, "_copy").toString();
            } else {
                absNewPath += "_copy";
            }
            destFile = new File(absNewPath);
        }

        try {
            if (!srcFile.isDirectory()) {
                Path temp = Files.copy(srcFile.toPath(), destFile.toPath());
                if (temp == null) {
                    throw new IOException();
                }
            } else {
                FileUtils.copyDirectory(srcFile, destFile);
            }

            return getRelativePath(destFile.getCanonicalPath(), rootDirectory);
        } catch (Exception e) {
            throw new IOException(path);
        }
    }

    public FileDTO uploadFile(MultipartFile multipartFile, String directoryPath) throws Exception {
        File directory = openWithCheck(getAbsolutePath(directoryPath, rootDirectory));
        if (!directory.isDirectory())
            throw new NotDirectoryException(directoryPath);

        String destinationPath = removeRepeatingSlashes(directory.getCanonicalPath() + "/" + multipartFile.getOriginalFilename());
        File file = new File(destinationPath);
        if (file.exists())
            throw new FileAlreadyExistsException(getRelativePath(file.getCanonicalPath(), rootDirectory));
        multipartFile.transferTo(file);
        return FileDTOConverter.convertToDTO(file, rootDirectory);
    }

    public FileDTO createDirectory(String directoryPath) throws Exception {
        if (hasForbiddenSymbols(directoryPath))
            throw new ForbiddenPathSymbolException(directoryPath);

        File directory = new File(getAbsolutePath(directoryPath, rootDirectory));
        if (directory.exists())
            throw new FileAlreadyExistsException(directoryPath);

        boolean result = directory.mkdir();

        if (!result)
            throw new IOException(directoryPath);

        return FileDTOConverter.convertToDTO(directory, rootDirectory);
    }

    private File openWithCheck(String path) throws Exception {
        if (hasForbiddenSymbols(path))
            throw new ForbiddenPathSymbolException(getRelativePath(path, rootDirectory));

        File file = new File(path);
        if (!file.exists())
            throw new NoSuchFileException(getRelativePath(path, rootDirectory));
        return file;
    }

    private boolean hasForbiddenSymbols(String path) {
        return path.contains("../") || path.contains("..\\");
    }


    public static String removeRepeatingSlashes(String str) {
        return str.replaceAll("((\\\\)|(/))+", "/");
    }

    public static String getAbsolutePath(String path, String rootDirectory) {
        String absolutePath = rootDirectory + path;
        return removeRepeatingSlashes(absolutePath);
    }

    public static String getRelativePath(String path, String rootDirectory) {
        path=removeRepeatingSlashes(path);
        String shorterRootDirectory = rootDirectory.substring(0, rootDirectory.length() - 1);
        if (shorterRootDirectory.equals(path))
            return "";
        else
            return removeRepeatingSlashes(path.replace(rootDirectory, ""));
    }
}
