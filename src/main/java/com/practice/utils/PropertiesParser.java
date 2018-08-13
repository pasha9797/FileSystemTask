package com.practice.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser {
    private final String propertiesFile = "fs-properties.properties";
    private final String rootDirectoryPropertyName = "root-directory";
    private final String initialPermissionsPropertyName = "initial-permissions";
    private final String minUsernameLengthPropertyName = "min-username-length";

    private Properties properties;

    private static PropertiesParser ourInstance;

    public static PropertiesParser getInstance() throws Exception {
        if (ourInstance == null) {
            ourInstance = new PropertiesParser();
        }
        return ourInstance;
    }

    private PropertiesParser() throws IOException {
        properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
        properties.load(inputStream);
    }

    public String getRootDirectory() throws IOException {
        String rootDir = properties.getProperty(rootDirectoryPropertyName);
        return rootDir;
    }

    public String[] getInitialPermissions() {
        String permissions = properties.getProperty(initialPermissionsPropertyName);

        if (permissions != null) {
            String[] permissionsList = permissions.split("#");
            return permissionsList;
        } else return new String[]{};
    }

    public int getMinUsernameLength() {
        String length = properties.getProperty(minUsernameLengthPropertyName);
        try {
            return Integer.parseInt(length);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
