package ru.markov.application.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
    static Connection conn = null;
    public PostgresConnection() {
        // URL подключения, имя пользователя и пароль
        String url = "jdbc:postgresql://localhost:5432/BrigApp"; // Замените your_database на имя вашей базы данных
        String user = "postgres"; // Замените your_username на ваше имя пользователя
        String password = "526452"; // Замените your_password на ваш пароль
        // Установка соединения
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Соединение с PostgreSQL установлено! Добро пожаловать.");
                conn = connection;
            }
        } catch (SQLException e) {
            System.out.println("Ошибка соединения: " + e.getMessage());
        }
    }
}
