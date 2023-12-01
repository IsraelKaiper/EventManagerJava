package com.example.eventmanager.controller;

import com.example.eventmanager.db.Database;
import com.example.eventmanager.model.EventModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField roadField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField aboutField;

    @FXML
    private void sendEvent() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String road = roadField.getText();
        String number = numberField.getText();
        String about = aboutField.getText();

        EventModel event = new EventModel();
        event.setNome(name);
        event.setTelefone(phone);
        event.setRua(road);
        event.setNumero(number);
        event.setSobre(about);

        if (name.isEmpty() || phone.isEmpty() || road.isEmpty() || number.isEmpty() || about.isEmpty()) {
            System.out.println("Os campos devem ser preenchidos.");
            return;
        }

        if (!isValidInteger(number)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText("Formato de número inválido.");
            alert.setContentText("O número deve ser inteiro.");
            alert.showAndWait();
            return;
        }

        try (Connection conexao = Database.getConnection()) {
            String sql = "INSERT INTO events (nome, telefone, rua, numero, sobre) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setString(1, event.getNome());
                stmt.setString(2, event.getTelefone());
                stmt.setString(3, event.getRua());
                stmt.setString(4, event.getNumero());
                stmt.setString(5, event.getSobre());
                stmt.executeUpdate();

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Sucesso");
                success.setHeaderText("Evento adicionado com sucesso.");
                success.showAndWait();

                nameField.setText("");
                phoneField.setText("");
                roadField.setText("");
                numberField.setText("");
                aboutField.setText("");

            } catch (SQLException e) {
                System.out.println("Erro ao conectar ao banco de dados.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.print("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    @FXML
    void goToEvent(ActionEvent event) {
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

    private boolean isValidInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
