package client.gui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.SimpleFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class SceneFactory {
    private TextField hostnameTextField;
    private TextField portTextField;
    private TextField filePathTextField;
    private TextArea resultTextArea;
    private TextField destinationTextField;

    private static class VBoxBuilder {
        private List<Node> nodeList;

        VBoxBuilder() {
            this.nodeList = new ArrayList<>();
        }

        private VBoxBuilder addNodesHorizontally(Node... nodes) {
            nodeList.add(new HBox(10, nodes));
            return this;
        }

        private VBox build() {
            Node[] nodes = new Node[nodeList.size()];
            nodes = nodeList.toArray(nodes);
            VBox vbox = new VBox(nodes);
            vbox.setMinWidth(MIN_WIDTH);
            vbox.setSpacing(10);
            return vbox;
        }
    }

    private static final double MIN_WIDTH = 400;

    Scene create() {
        VBoxBuilder vBoxBuilder = new VBoxBuilder();


        TableView<SimpleFile> table = new TableView<>();


        ObservableList<SimpleFile> teamMembers = FXCollections.observableArrayList();
        table.setItems(teamMembers);

        hostnameTextField = new TextField();
        portTextField = new TextField();
        destinationTextField = new TextField();
        filePathTextField = new TextField();
        Button getRequestButton = new Button("Get file");
        getRequestButton.setOnMouseClicked(this::performGetRequest);
        Button listRequestButton = new Button("List content");
        listRequestButton.setOnMouseClicked(this::performListRequest);
        resultTextArea = new TextArea("hey\nmay");


        TableColumn<SimpleFile,String> firstNameCol = new TableColumn<>("Filename");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<SimpleFile,String> lastNameCol = new TableColumn<>("Is directory");
        lastNameCol.setCellValueFactory(new PropertyValueFactory("isDirectory"));

        table.getColumns().setAll(firstNameCol, lastNameCol);

        VBox root = vBoxBuilder.addNodesHorizontally(new Label("Hostname:"), hostnameTextField, new Label("Port:"), portTextField)
                .addNodesHorizontally(new Label("Download directory:"), destinationTextField)
                .addNodesHorizontally(filePathTextField, getRequestButton, listRequestButton)
                .addNodesHorizontally(resultTextArea)
                .build();
        return new Scene(root);
    }

    private void performListRequest(MouseEvent mouseEvent) {
        System.err.println("listing");
        try {
            Client client = Client.getNonBlocking();
            System.err.println("connecting");
            client.connect(hostnameTextField.getText(), Integer.parseInt(portTextField.getText()));
            System.err.println("requesting");
            List<SimpleFile> result = client.executeList(filePathTextField.getText());
            StringBuilder resultString = new StringBuilder();
            for (SimpleFile file : result) {
                //noinspection StringConcatenationInsideStringBufferAppend
                resultString.append(file.name + " " + (file.isDirectory ? "directory" : "file") + "\n");
            }
            System.err.println("result:");
            System.err.println(resultString.toString());
            resultTextArea.setText(resultString.toString());
            client.disconnect();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error while performing list request: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.show();
    }

    private void performGetRequest(MouseEvent mouseEvent) {
        System.err.println("listing");
        try {
            Client client = Client.getNonBlocking();
            System.err.println("connecting");
            client.connect(hostnameTextField.getText(), Integer.parseInt(portTextField.getText()));
            System.err.println("requesting");
            String destination = destinationTextField.getText();
            Path result = client.executeGet(filePathTextField.getText(),
                    destination.isEmpty() ? null : destination);
            System.err.println("result:");
            System.err.println(result.toAbsolutePath().toString());
            showAlert(Alert.AlertType.INFORMATION, result.toAbsolutePath().toString());
            client.disconnect();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error while performing list request: " + e.getMessage());
        }
    }
}
