package com.example.eventmanager.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.eventmanager.db.Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void login(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (authenticateUser(email, password)) {
            navigateNextScreen(event);
        } else {
            Alert errorAuth = new Alert(Alert.AlertType.ERROR);
            errorAuth.setTitle("Erro de autenticação");
            errorAuth.setHeaderText("Email ou senha estao incorretos");
            errorAuth.setContentText("Tente novamente");
            errorAuth.showAndWait();
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/eventmanager/Register.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticateUser(String email, String password) {
        try (Connection connection = Database.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("senha");

                    if (password.equals(storedPassword)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void navigateNextScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/eventmanager/Event.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
