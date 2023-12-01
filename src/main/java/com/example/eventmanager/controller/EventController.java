package com.example.eventmanager.controller;

import com.example.eventmanager.db.Database;
import com.example.eventmanager.model.EventModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.lang.Integer.parseInt;

public class EventController {
    @FXML
    private TableView<EventModel> table;
    @FXML
    private TableColumn<EventModel, String> nome, telefone, rua, sobre;
    @FXML
    private TableColumn<EventModel, Integer> numero;

    ObservableList<EventModel> events = FXCollections.observableArrayList();

    public void initialize() {
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        telefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        rua.setCellValueFactory(new PropertyValueFactory<>("rua"));
        numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        sobre.setCellValueFactory(new PropertyValueFactory<>("sobre"));

        // Adicionando um estilo na tabela
        Label labelNome = new Label("NOME");
        Label labelTelefone = new Label("TELEFONE");
        Label labelRua = new Label("RUA");
        Label labelNumero = new Label("NÚMERO");
        Label labelSobre = new Label("SOBRE");

        labelNome.setStyle("-fx-text-fill: #6495ED;");
        labelTelefone.setStyle("-fx-text-fill: #6495ED;");
        labelRua.setStyle("-fx-text-fill: #6495ED;");
        labelNumero.setStyle("-fx-text-fill: #6495ED;");
        labelSobre.setStyle("-fx-text-fill: #6495ED;");

        nome.setGraphic(labelNome);
        telefone.setGraphic(labelTelefone);
        rua.setGraphic(labelRua);
        numero.setGraphic(labelNumero);
        sobre.setGraphic(labelSobre);

        // Exibindo os eventos do banco de dados
        listEvents();
    }

    public void listEvents() {
        String sql = "SELECT * FROM events";

        try (Connection conexao = Database.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Limpando a lista de eventos
            events.clear();

            // Pegando os eventos do banco de dados
            while (rs.next()) {
                EventModel event = new EventModel();
                event.setNome(rs.getString("nome"));
                event.setTelefone(rs.getString("telefone"));
                event.setRua(rs.getString("rua"));
                event.setNumero(rs.getString("numero"));
                event.setSobre(rs.getString("sobre"));

                events.add(event);
            }

            // Definindo a lista de eventos na tabela
            table.setItems(events);

        } catch (SQLException e) {
            Alert errorConn = new Alert(Alert.AlertType.ERROR);
            errorConn.setTitle("Erro");
            errorConn.setHeaderText("Erro ao conectar ao banco de dados.");
            errorConn.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAddEvent(ActionEvent event) {
        try {
            // Carregando o arquivo FXML da tela
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/eventmanager/AddEvent.fxml"));
            Parent root = loader.load();

            // Criando a cena da tela
            Scene scene = new Scene(root);

            // Acessando o palco (Stage) atual da aplicação
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Definindo a cena da tela no palco
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------- Editar e apagar evento ----------
    @FXML
    private TextField nameField, phoneField, roadField, numberField, aboutField;

    @FXML
    protected void lineTable(MouseEvent event) {
        EventModel selectedEvent = table.getSelectionModel().getSelectedItem();

        nameField.setText(selectedEvent.getNome());
        phoneField.setText(selectedEvent.getTelefone());
        roadField.setText(selectedEvent.getRua());
        numberField.setText(String.valueOf(selectedEvent.getNumero()));
        aboutField.setText(selectedEvent.getSobre());
    }


    @FXML
    void deleteAction(ActionEvent event) {
        EventModel selectedEvent = table.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            String sql = "DELETE FROM events WHERE nome = ?";

            try (Connection conexao = Database.getConnection();
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setString(1, selectedEvent.getNome());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    Alert successDel = new Alert(Alert.AlertType.INFORMATION);
                    successDel.setTitle("Sucesso");
                    successDel.setHeaderText("Evento excluído com sucesso.");
                    successDel.showAndWait();

                    listEvents();  // Atualiza a tabela após exclusão
                    nameField.setText("");
                    phoneField.setText("");
                    roadField.setText("");
                    numberField.setText("");
                    aboutField.setText("");
                } else {
                    Alert errorDel = new Alert(Alert.AlertType.ERROR);
                    errorDel.setTitle("Erro");
                    errorDel.setHeaderText("Erro ao excluir o evento.");
                    errorDel.showAndWait();
                }

            } catch (SQLException e) {
                Alert errorConn = new Alert(Alert.AlertType.ERROR);
                errorConn.setTitle("Erro");
                errorConn.setHeaderText("Erro ao conectar ao banco de dados.");
                errorConn.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert warningDel = new Alert(Alert.AlertType.WARNING);
            warningDel.setTitle("Aviso");
            warningDel.setHeaderText("Nenhum evento selecionado para exclusão.");
            warningDel.showAndWait();
        }
    }

    @FXML
    void editAction(ActionEvent event) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String road = roadField.getText();
        String number = String.valueOf(parseInt(numberField.getText()));
        String about = aboutField.getText();

        EventModel selectedEvent = table.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            String sql = "UPDATE events SET nome = ?, telefone = ?, rua = ?, numero = ?, sobre = ? WHERE nome = ?";
            try (Connection conexao = Database.getConnection();
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, road);
                stmt.setString(4, number);
                stmt.setString(5, about);
                stmt.setString(6, selectedEvent.getNome());

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    Alert successEdit = new Alert(Alert.AlertType.INFORMATION);
                    successEdit.setTitle("Sucesso");
                    successEdit.setHeaderText("Evento atualizado com sucesso.");
                    successEdit.showAndWait();

                    listEvents();
                } else {
                    Alert errorEdit = new Alert(Alert.AlertType.ERROR);
                    errorEdit.setTitle("Erro");
                    errorEdit.setHeaderText("Erro ao atualizar o evento.");
                    errorEdit.showAndWait();
                }

            } catch (SQLException e) {
                Alert errorConn = new Alert(Alert.AlertType.ERROR);
                errorConn.setTitle("Erro");
                errorConn.setHeaderText("Erro ao conectar ao banco de dados.");
                errorConn.showAndWait();
                e.printStackTrace();
            }
        } else {
            Alert warningEdit = new Alert(Alert.AlertType.WARNING);
            warningEdit.setTitle("Aviso");
            warningEdit.setHeaderText("Nenhum evento selecionado para edição.");
            warningEdit.showAndWait();
        }
    }

    @FXML
    void exitAction(ActionEvent event) {
        try {
            Alert exit = new Alert(Alert.AlertType.CONFIRMATION);
            exit.setTitle("Sair");
            exit.setHeaderText("Deseja realmente sair?");
            exit.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> response = exit.showAndWait();
            if (response.isPresent() && response.get() == ButtonType.YES) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/eventmanager/Login.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
