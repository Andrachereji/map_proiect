package com.example.guiex1.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Constants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final LocalDate CURRENT_TIME = LocalDate.now();

    public static InputStream input = null;

    public static String url = "jdbc:postgresql://localhost:5432/socialnetwork";
    public static String username = "postgres";
    public static String password = "Andra.2002";

    static {
        try {
            input = new FileInputStream(
                    "src\\main\\resources\\ro\\ubbcluj\\map\\config.properties");
            Properties properties;
            properties = new Properties();

            properties.load(input);

            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

