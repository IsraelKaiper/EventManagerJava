package com.example.eventmanager.controller;

import com.example.eventmanager.model.RegisterModel;
import com.example.eventmanager.model.ValidationModel;
import com.example.eventmanager.db.Database;
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    private ValidationModel validator = new ValidationModel();

    @FXML
    void register(ActionEvent event) {
        RegisterModel register = new RegisterModel();
        register.setEmail(emailField.getText());
        register.setPassword(passwordField.getText());
        register.setConfirmPassword(confirmPasswordField.getText());

        // Validar email
        if (!validator.isValidEmail(register.getEmail())) {
            showErrorAlert("Formato de email inválido.");
            return;
        }

        // Validar senha
        if (!validator.isValidPassword(register.getPassword())) {
            showErrorAlert("A senha deve ter pelo menos 8 caracteres.");
            return;
        }

        if (!register.getPassword().equals(register.getConfirmPassword())) {
            showErrorAlert("As senhas não coincidem.");
            return;
        }

        // Registrar usuário
        try (Connection conexao = Database.getConnection()) {
            String sql = "INSERT INTO usuarios (email, senha) VALUES (?, ?)";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setString(1, register.getEmail());
                stmt.setString(2, register.getConfirmPassword());

                stmt.executeUpdate();

                emailField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Sucesso");
                success.setHeaderText("Usuário registrado com sucesso.");
                success.showAndWait();

                goToLogin(event);

            } catch (SQLException e) {
                Alert errorRegister = new Alert(Alert.AlertType.ERROR);
                errorRegister.setTitle("Erro");
                errorRegister.setHeaderText("Erro ao registrar usuário.");
                errorRegister.showAndWait();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.print("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/eventmanager/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
