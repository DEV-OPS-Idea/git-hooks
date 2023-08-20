package com.java.git.hook.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {

    public String getValues(String propertyPath, String property) throws IOException {
        InputStream in = this.getClass().getResourceAsStream(propertyPath);
        Properties properties = new Properties();
        properties.load(in);
        String propertyValue = properties.getProperty(property);
        return  propertyValue;
    }

    public static boolean validRegex(String pattern, String message){
        Pattern patterns = Pattern.compile(pattern);
        Matcher matcher = patterns.matcher(message);
        boolean validMessage = matcher.matches();
        return validMessage;
    }
}
