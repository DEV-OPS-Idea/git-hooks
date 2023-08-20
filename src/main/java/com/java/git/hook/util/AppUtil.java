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
        //System.out.println("propertyValue:: " + propertyValue);
        return propertyValue;
    }
    public static boolean validRegex(String pattern,String message){
        Pattern patten = Pattern.compile(pattern);
        Matcher matcher = patten.matcher(message);
        boolean validMessage = matcher.matches();
        return validMessage;
    }
}
