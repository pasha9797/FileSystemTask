package com.practice.model.request;

public class UpdateFileRequest {
    public enum UpdateFileOption {
        COPY,
        MOVE
    }

    private String path;
    private String newPath;
    private UpdateFileOption option;

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

    public UpdateFileOption getOption() {
        return option;
    }

    public void setOption(UpdateFileOption option) {
        this.option = option;
    }
}
