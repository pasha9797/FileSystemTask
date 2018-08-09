package com.practice.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesParser {
    private final String propertiesFile = "fs-properties.properties";
    private final String rootDirectoryPropertyName = "root-directory";

    private static PropertiesParser ourInstance = new PropertiesParser();

    public static PropertiesParser getInstance() {
        return ourInstance;
    }

    private PropertiesParser() {
    }

    public String getRootDirectory() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
        properties.load(inputStream);
        String res= properties.getProperty(rootDirectoryPropertyName);
        return res;
    }
}
