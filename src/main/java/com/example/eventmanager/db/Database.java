package com.example.eventmanager.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/EventManager?useSSL=false";
    private static final String USUARIO = "Israel";
    private static final String SENHA = "123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}