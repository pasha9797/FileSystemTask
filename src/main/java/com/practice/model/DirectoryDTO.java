package com.practice.model;

import java.util.List;

public class DirectoryDTO extends FileDTO{
    List<String> contentFiles;

    public List<String> getContentFiles() {
        return contentFiles;
    }

    public void setContentFiles(List<String> contentFiles) {
        this.contentFiles = contentFiles;
    }
}
