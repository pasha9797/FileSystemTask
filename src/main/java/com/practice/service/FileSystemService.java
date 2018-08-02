package com.practice.service;

import com.practice.model.FileDTO;
import com.practice.model.FileDTOConverter;
import com.practice.other.PropertiesParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileSystemService {
    private String rootDirectory;

    public FileSystemService() {
        PropertiesParser parser = PropertiesParser.getInstance();
        try {
            rootDirectory = parser.getRootDirectory();
            rootDirectory += '\\';
            rootDirectory = removeRepeatingSlashes(rootDirectory);
            File file = new File(rootDirectory);
            if (!file.exists())
                throw new NoSuchFileException("Specified root directory does not exist: " + rootDirectory);
            if (!file.isDirectory())
                throw new NotDirectoryException("Specified root directory is not a directory: " + rootDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            rootDirectory = ".\\"; // add slash so that concatenation with relative paths will be available
        }
    }

    public FileDTO getFileDTO(String path) throws IOException {
        File file = openWithCheck(getAbsolutePath(path));
        return FileDTOConverter.convertToDTO(file, rootDirectory);
    }

    public List<FileDTO> getDirectoryContent(String path) throws IOException{
        File directory = openWithCheck(getAbsolutePath(path));
        if (!directory.isDirectory())
            throw new NotDirectoryException("Specified directory is not a directory");

        List<FileDTO> content = new ArrayList<FileDTO>();
        for(File child: directory.listFiles()){
            content.add(FileDTOConverter.convertToDTO(child, rootDirectory));
        }

        return content;
    }

    public String readTextFile(String path) throws IOException {
        Pattern p = Pattern.compile("^.+\\.(txt|rtf)$");
        Matcher m = p.matcher(path);
        if (!m.matches())
            throw new IOException("Selected file is not a text file");

        File file = openWithCheck(getAbsolutePath(path));

        String data;
        data = new String(Files.readAllBytes(file.toPath()));
        return data;
    }

    public void removeFile(String path) throws IOException {
        File file = openWithCheck(getAbsolutePath(path));
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
        if (hasForbiddenSymbols(newName))
            throw new IOException("Forbidden symbols found in new name");

        File file = openWithCheck(getAbsolutePath(path));
        String canonicalPath = file.getCanonicalPath();
        String newPath = canonicalPath.substring(0, canonicalPath.lastIndexOf('\\') + 1) + newName;
        File file2 = new File(newPath);

        if (file2.exists())
            throw new FileAlreadyExistsException("File or directory with such name already exists");

        boolean result = file.renameTo(file2);

        if (!result) {
            throw new IOException("Unable to rename file or directory");
        }
    }

    public void moveFile(String path, String newPath, Boolean keepOld) throws IOException {
        try {
            if (hasForbiddenSymbols(path))
                throw new IOException("Forbidden symbols found in old path");
            if (hasForbiddenSymbols(newPath))
                throw new IOException("Forbidden symbols found in new path");

            if (!keepOld) {
                Path temp = Files.move(Paths.get(getAbsolutePath(path)), Paths.get(getAbsolutePath(newPath)));
                if (temp == null) {
                    throw new IOException("Unable to move file or directory");
                }
            } else {
                Path temp = Files.copy(Paths.get(getAbsolutePath(path)), Paths.get(getAbsolutePath(newPath)));
                if (temp == null) {
                    throw new IOException("Unable to copy file or directory");
                }
            }
        } catch (FileAlreadyExistsException e) {
            throw new FileAlreadyExistsException("File or directory with such name already exists");
        } catch (NoSuchFileException e) {
            throw new NoSuchFileException("No such file or directory");
        }
    }

    public void uploadFile(MultipartFile multipartFile, String directoryPath) throws IOException {
        File directory = openWithCheck(getAbsolutePath(directoryPath));
        if (!directory.isDirectory())
            throw new NotDirectoryException("Specified directory is not a directory");

        String destinationPath = directory.getCanonicalPath() + "\\" + multipartFile.getOriginalFilename();
        File file = new File(destinationPath);
        if (file.exists())
            throw new FileAlreadyExistsException("File with such name already exists");
        multipartFile.transferTo(file);
    }

    public void createDirectory(String directoryPath) throws IOException {
        if (hasForbiddenSymbols(directoryPath))
            throw new IOException("Forbidden symbols found in path");

        File directory = new File(getAbsolutePath(directoryPath));
        if(directory.exists())
            throw new FileAlreadyExistsException("Directory already exists");

        boolean result = directory.mkdir();

        if (!result)
            throw new IOException("Unable to create directory");
    }

    private File openWithCheck(String path) throws IOException {
        if (hasForbiddenSymbols(path))
            throw new IOException("Forbidden symbols found in path");

        File file = new File(path);
        if (!file.exists())
            throw new NoSuchFileException("No such file or directory");
        return file;
    }

    private boolean hasForbiddenSymbols(String path) {
        return path.contains("../") || path.contains("..\\");
    }

    private String getAbsolutePath(String path) {
        String absolutePath = rootDirectory + path;
        return removeRepeatingSlashes(absolutePath);
    }

    private String removeRepeatingSlashes(String str) {
        return str.replaceAll("((\\\\)|(/))+", "\\\\");
    }
}
