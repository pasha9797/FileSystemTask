define({ "api": [
  {
    "type": "post",
    "url": "/api/files",
    "title": "Create file or directory",
    "name": "CreateFile",
    "description": "<p>Upload new file or create a directory.</p>",
    "group": "Files",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "directoryPath",
            "description": "<p>Path to directory where to upload new file, or path to the directory to create.</p>"
          },
          {
            "group": "Parameter",
            "type": "File",
            "optional": true,
            "field": "file",
            "description": "<p>File to upload. If not set, creation of directory at path directoryPath will be attempted.</p>"
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/main/java/com/practice/controller/FileSystemController.java",
    "groupTitle": "Files"
  },
  {
    "type": "delete",
    "url": "/api/files",
    "title": "Delete file or directory",
    "name": "DeleteFile",
    "description": "<p>Delete specified file or directory.</p>",
    "group": "Files",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "path",
            "description": "<p>Path to the file.</p>"
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/main/java/com/practice/controller/FileSystemController.java",
    "groupTitle": "Files"
  },
  {
    "type": "get",
    "url": "/api/files/content",
    "title": "Get file or directory content",
    "name": "GetFileContent",
    "description": "<p>Get content of text file or content of directory.</p>",
    "group": "Files",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "path",
            "description": "<p>Path to the file. If specified file is directory, response will contain array of json objects describing each file within directory. If specified file is text file, response will contain plain text from file.</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success Directory content",
          "content": "HTTP/1.1 200 OK\n[\n{\n\"path\": \".android\",\n\"size\": 0,\n\"creationDate\": 1529564019132,\n\"lastModifiedDate\": 1529564381823,\n\"lastAccessDate\": 1529564381823,\n\"directory\": true\n},\n{\n\"path\": \".bash_history\",\n\"size\": 75,\n\"creationDate\": 1533217156161,\n\"lastModifiedDate\": 1533218627613,\n\"lastAccessDate\": 1533217156161,\n\"directory\": false\n}\n]",
          "type": "json"
        },
        {
          "title": "Success Text file",
          "content": "HTTP/1.1 200 OK\nContent-Type: text/plain;charset=ISO-8859-1\nContent of the file will follow here.",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/main/java/com/practice/controller/FileSystemController.java",
    "groupTitle": "Files"
  },
  {
    "type": "get",
    "url": "/api/files",
    "title": "Get file or directory description",
    "name": "GetFileInfo",
    "description": "<p>Get information about file or directory in json format.</p>",
    "group": "Files",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "path",
            "description": "<p>Path to the file.</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success response",
          "content": "HTTP/1.1 200 OK\n{\n\"path\": \"Desktop\",\n\"size\": 4096,\n\"creationDate\": 1529534686391,\n\"lastModifiedDate\": 1533468452927,\n\"lastAccessDate\": 1533468452927,\n\"directory\": true\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/main/java/com/practice/controller/FileSystemController.java",
    "groupTitle": "Files"
  },
  {
    "type": "put",
    "url": "/api/files",
    "title": "Update file or directory",
    "name": "UpdateFile",
    "description": "<p>Update file or directory according to specified option.</p>",
    "group": "Files",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "path",
            "description": "<p>Path to the file.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "newPath",
            "description": "<p>New path to the file - where to move/copy file/</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "allowedValues": [
              "\"COPY\"",
              "\"MOVE\""
            ],
            "optional": false,
            "field": "option",
            "description": "<p>Option specifying what to do with file.</p>"
          }
        ]
      },
      "examples": [
        {
          "title": "Request example",
          "content": "{\n\"path\": \"Desktop/test.exe\",\n\"newPath\": \"Desktop/prod.exe\",\n\"option\": \"MOVE\"\n}",
          "type": "json"
        }
      ]
    },
    "version": "0.0.0",
    "filename": "src/main/java/com/practice/controller/FileSystemController.java",
    "groupTitle": "Files"
  }
] });