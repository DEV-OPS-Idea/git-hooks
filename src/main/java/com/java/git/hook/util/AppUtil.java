package com.java.git.hook.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppUtil {
    public String getValues(String propertyPath, String property) throws IOException {
        InputStream in = this.getClass().getResourceAsStream(propertyPath);
        Properties properties = new Properties();
        properties.load(in);
        String propertyValue = properties.getProperty(property);
        //System.out.println("propertyValue:: " + propertyValue);
        return propertyValue;
    }
}
