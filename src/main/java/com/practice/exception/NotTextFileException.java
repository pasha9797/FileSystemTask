package com.practice.exception;

public class NotTextFileException extends Exception{
    private String message, fileType;

    public NotTextFileException(String message, String fileType){
        super(message);
        this.message=message;
        this.fileType=fileType;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getFileType() {
        return fileType;
    }

}
